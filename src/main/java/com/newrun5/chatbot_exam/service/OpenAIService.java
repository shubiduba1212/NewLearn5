package com.newrun5.chatbot_exam.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {

    @Value("${openai.apiKey}")
    private String apiKey;

    public String generateQuestion(String content) {
        // OpenAI API 호출 로직 구현
        return "생성된 예상 문제: " + content.substring(0, Math.min(50, content.length())) + "...";
    }
}

