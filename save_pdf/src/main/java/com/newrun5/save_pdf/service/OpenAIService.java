package com.newrun5.save_pdf.service;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {
    @Value("${spring.openai.api-key}")
    private String apiKey;

    public String generateEmbedding(String text) {
        OpenAiService service = new OpenAiService(apiKey);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("다음 텍스트를 임베딩하세요: " + text)
                .model("text-davinci-002")
                .maxTokens(100)
                .build();
        return service.createCompletion(completionRequest).getChoices().get(0).getText();
    }

    public String generateResponse(String query, String context) {
        OpenAiService service = new OpenAiService(apiKey);
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt("맥락: " + context + "\n질문: " + query + "\n답변:")
                .model("text-davinci-002")
                .maxTokens(100)
                .build();
        return service.createCompletion(completionRequest).getChoices().get(0).getText();
    }
}
