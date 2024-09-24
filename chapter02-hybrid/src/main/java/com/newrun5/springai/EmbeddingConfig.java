package com.newrun5.springai;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingConfig
{
    // application.properties 에서 token을 가져옴
    @Value("${spring.ai.openai.api-key}")
    private String openAIToken;

    @Bean
    public EmbeddingModel embeddingModel()
    {
        return new OpenAiEmbeddingModel(new OpenAiApi(openAIToken));
    }
}
