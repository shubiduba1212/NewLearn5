package com.newrun5.save_pdf.service;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.embedding.Embedding;
import com.theokanning.openai.embedding.EmbeddingRequest;
import com.theokanning.openai.embedding.EmbeddingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAIEmbeddingService {

    private final OpenAiService openAiService;

    public OpenAIEmbeddingService(@Value("${spring.openai.api-key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey);
    }

    public List<Double> getEmbedding(String text) {
        // EmbeddingRequest 생성
        EmbeddingRequest request = EmbeddingRequest.builder()
                .model("text-embedding-ada-002")  // 임베딩 모델 지정
                .input(List.of(text))  // String을 List<String>으로 변환
                .build();

        try {
            // Embedding 결과 가져오기
            List<Embedding> embeddings = openAiService.createEmbeddings(request).getData();
            return embeddings.get(0).getEmbedding();
        } catch (Exception e) {
            // 예외 처리: 임베딩 요청이 실패했을 때
            throw new RuntimeException("임베딩 요청 실패: " + e.getMessage(), e);
        }
    }
}
