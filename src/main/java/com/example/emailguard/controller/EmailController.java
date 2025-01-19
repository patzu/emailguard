package com.example.emailguard.controller;

import com.example.emailguard.model.EmailRequest;
import com.example.emailguard.model.ProcessedEmailResponse;
import com.example.emailguard.service.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emailguard")
public class EmailController {

    private final OpenAIService openAIService;

    @Autowired
    public EmailController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/process")
    public ProcessedEmailResponse processEmail(@RequestBody EmailRequest emailRequest) {

        // Pass the email content and rules to the service
        return openAIService.processEmail(emailRequest);
    }
}