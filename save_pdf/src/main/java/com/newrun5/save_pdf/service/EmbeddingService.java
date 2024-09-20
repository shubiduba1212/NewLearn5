//package com.newrun5.save_pdf.service;
//
//import com.theokanning.openai.OpenAiService;
//import com.theokanning.openai.embedding.EmbeddingRequest;
//import com.theokanning.openai.embedding.EmbeddingResult;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class EmbeddingService {
//
//    private final OpenAiService openAiService;
//
//    public EmbeddingService(OpenAiService openAiService) {
//        this.openAiService = openAiService;
//    }
//
//    // 텍스트 임베딩 생성 메서드
//    public float[] getTextEmbedding(String text) {
//        EmbeddingRequest embeddingRequest = EmbeddingRequest.builder()
//                .input(List.of(text))
//                .model("text-embedding-ada-002")
//                .build();
//
//        EmbeddingResult embeddingResult = openAiService.createEmbeddings(embeddingRequest);
//        List<Double> embedding = embeddingResult.getData().get(0).getEmbedding();
//
//        // List<Double>를 float[]로 변환
//        float[] embeddingArray = new float[embedding.size()];
//        for (int i = 0; i < embedding.size(); i++) {
//            embeddingArray[i] = embedding.get(i).floatValue();
//        }
//
//        return embeddingArray;
//    }
//
//    // 이미지 임베딩 생성 메서드 (예시)
//    public float[] getImageEmbedding(String imageData) {
//        // 이미지 임베딩 생성 로직을 구현하세요.
//        return new float[0];  // 예시로 빈 배열 반환
//    }
//
//    // 데이터 저장 메서드
//    public void saveDataItem(String title, String text, float[] textEmbedding, float[] imageEmbedding) {
//        // MongoDB에 데이터 저장하는 로직 구현
//    }
//
//    // 임베딩 생성 메서드
//    public String generateEmbedding(String text) {
//        EmbeddingRequest embeddingRequest = EmbeddingRequest.builder()
//                .input(List.of(text))
//                .model("text-embedding-ada-002")
//                .build();
//
//        EmbeddingResult embeddingResult = openAiService.createEmbeddings(embeddingRequest);
//        List<Double> embedding = embeddingResult.getData().get(0).getEmbedding();
//
//        // List<Double>를 String 형태로 변환 (저장 및 출력용)
//        return embedding.toString();
//    }
//}
//
//
//
//
//
