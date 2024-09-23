package com.newrun5.chatbot_exam.service;

import com.newrun5.chatbot_exam.model.HistoryData;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private OpenAIService openAIService;

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    public List<String> handleMessage(String message, int count) {
        // 메시지 분석
        if (message.contains("문제 출제") || message.contains("문제 내줘")) {
            // MongoDB에서 데이터 조회
            List<HistoryData> dataList = fetchHistoryData();

            // OpenAI API를 사용하여 문제 생성
            return dataList.stream()
                    .limit(count)
                    .map(data -> openAIService.generateQuestion(data.getContent()))
                    .collect(Collectors.toList());
        } else {
            return List.of("질문에 '문제 출제' 또는 '문제 내줘'라는 말이 포함되어 있지 않습니다.");
        }
    }

    private List<HistoryData> fetchHistoryData() {
        List<HistoryData> dataList = new ArrayList<>();

        try (var mongoClient = MongoClients.create(mongoUri)) {
            MongoDatabase database = mongoClient.getDatabase("vectordb");
            MongoCollection<Document> collection = database.getCollection("history_data");

            for (Document doc : collection.find()) {
                HistoryData data = new HistoryData();
                data.setId(doc.getObjectId("_id").toString());
                data.setTitle(doc.getString("title"));
                data.setContent(doc.getString("content"));
                // 임베딩 데이터는 필요 시 파싱하여 추가
                dataList.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }
}


