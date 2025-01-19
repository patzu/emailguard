package com.example.emailguard.model;

import java.util.List;

public record ProcessedEmailResponse(String processedContent, List<String> suggestions) {
}
