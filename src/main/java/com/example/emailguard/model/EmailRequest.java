package com.example.emailguard.model;

import java.util.List;

public record EmailRequest(String content, List<String> rules) {
}
