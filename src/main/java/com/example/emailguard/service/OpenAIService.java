package com.example.emailguard.service;

import com.example.emailguard.exception.InvalidEmailException;
import com.example.emailguard.model.EmailRequest;
import com.example.emailguard.model.ProcessedEmailResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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

    public ResponseEntity<String> processEmail(EmailRequest emailRequest) {
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

        // Messages array for API request
        ArrayNode messages = requestBody.putArray("messages");

        // Add system message
        messages.addObject()
                .put("role", "system")
                .put("content", "You are an assistant designed to help improve email quality by analyzing the content based on " +
                        "specific financial rules. Given the following email content and rules, analyze each sentence explicitly and apply the " +
                        "severity levels (red, yellow, green) based on whether they breach any of the rules." +
                        "- Green: The sentence is correct and follows the rules." +
                        "- Yellow: The sentence is somewhat vague or could lead to ambiguity, breaching the rules in some way.- " +
                        "- Red: The sentence is high-risk or unclear, breaching the rules in a significant way.For any sentences that " +
                        "breach the rules, add suggestions for improvement in parentheses next to the sentence. Ensure that the rules are clearly " +
                        "numbered in your response and identify which rule was breached." + "Here is the sample output html placeholder "+

                        "<p class=\"green\">{Sentence}</p>" +
                        "<p class=\"yellow\"><strong>{sentence is here}</strong> (Breached Rule #1})</p>" +
                        "<p class=\"suggestion\">Suggested Fix: \"Suggested fix.\" </p>" +
                        "<p class=\"red\"><strong></strong> (Breached Rule #1, Breached Rule #3)</p>" +
                        "<p class=\"suggestion\">Suggested Fix: {Here is suggested fix} </p>"
                        +

                        "Here are the rules: " + rulesBuilder.toString());

        // Add user message
        messages.addObject()
                .put("role", "user")
                .put("content", emailRequest.content());

        // Convert to String
        String requestPayload = requestBody.toString();

        // Create HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openaiApiKey);  // Add Authorization header
        headers.set("Content-Type", "application/json");

        // Create HttpEntity with headers and request body
        HttpEntity<String> entity = new HttpEntity<>(requestPayload, headers);

        // Send the request and get the response (directly returning the ResponseEntity)
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl).toUriString();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Return the response directly (since it's already in HTML format)
        return response;
    }

}
