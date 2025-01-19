package com.example.emailguard.service;

import com.example.emailguard.exception.InvalidEmailException;
import com.example.emailguard.model.EmailRequest;
import com.example.emailguard.model.ProcessedEmailResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class
OpenAIService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.ai.openai.api-key}")
    private String openaiApiKey;

    @Value("${spring.ai.openai.api-url}")
    private String apiUrl;

    @Value("${spring.ai.openai.model}")
    private String model;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();  // Jackson ObjectMapper
    }

    public ProcessedEmailResponse processEmail(EmailRequest emailRequest) {
        // Check for invalid email content (custom validation logic)
        if (emailRequest.content().isEmpty()) {
            throw new InvalidEmailException("Email content cannot be empty");
        }

        // Prepare the request payload by adding rules and email content
        StringBuilder rulesBuilder = new StringBuilder();
        for (String rule : emailRequest.rules()) {
            rulesBuilder.append(rule).append("\n");
        }

        // Build the payload with Jackson
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", model);

        // Messages array
        requestBody.putArray("messages").addObject().put("role", "system").put("content", "You are an assistant that helps improve email quality by applying the following rules:\n" + rulesBuilder.toString());
        requestBody.putArray("messages").addObject().put("role", "user").put("content", emailRequest.content());
        requestBody.putArray("messages").addObject().put("role", "system").put("content", "Please determine the severity of each sentence and suggest any necessary corrections. Use the following color coding for the severity: red for dangerous, yellow for risky, and green for safe. If any sentence needs correction, suggest a fix in parentheses after the sentence.");

        // Convert to String
        String requestPayload = requestBody.toString();

        // Create HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openaiApiKey);  // Add Authorization header
        headers.set("Content-Type", "application/json");

        // Create HttpEntity with headers and request body
        HttpEntity<String> entity = new HttpEntity<>(requestPayload, headers);

        // Send the request and get the response
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl).toUriString();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Convert the response to HTML format with color coding and suggested fixes
        String htmlResponse = processResponseToHtml(response.getBody());

        // Return the processed email response
        return new ProcessedEmailResponse(htmlResponse, List.of("Suggested correction"));
    }

    private String processResponseToHtml(String response) {
        // Here, we assume the response from OpenAI is already formatted in the desired way,
        // where each sentence has the color (red, yellow, or green) and the suggested fix is in parentheses.

        // Example response could look like:
        // "sentence 1: {color: 'red', fix: 'Suggested fix here'}"
        // "sentence 2: {color: 'yellow', fix: 'Suggested fix here'}"

        String[] sentences = response.split("\\.");

        StringBuilder htmlResponse = new StringBuilder();

        for (String sentence : sentences) {
            // Default to green if no color found
            String color = "green";
            String suggestedFix = "";

            // Example logic to parse the response (this will need to be adjusted depending on how OpenAI structures the response)
            if (sentence.contains("{color: 'red'")) {
                color = "red";
                suggestedFix = extractSuggestedFix(sentence);  // Method to extract fix from OpenAI's response
            } else if (sentence.contains("{color: 'yellow'")) {
                color = "yellow";
                suggestedFix = extractSuggestedFix(sentence);
            }

            // Add the sentence to the HTML response with the appropriate color
            if (!suggestedFix.isEmpty()) {
                htmlResponse.append("<p style='color:" + color + "'>")
                        .append(sentence.trim())
                        .append(" <span style='color:blue'>(")
                        .append(suggestedFix)
                        .append(")</span></p>");
            } else {
                htmlResponse.append("<p style='color:" + color + "'>")
                        .append(sentence.trim())
                        .append("</p>");
            }
        }

        return htmlResponse.toString();
    }

    private String extractSuggestedFix(String sentence) {
        // Logic to extract the suggested fix from OpenAI's response
        // This could involve regex or some string parsing depending on how OpenAI structures the fix in the response
        return "Suggested fix for this problematic sentence";
    }
}
