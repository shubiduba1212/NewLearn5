package com.newrun5.create_cur_ver2.service;

import com.newrun5.create_cur_ver2.model.DataItem;
import com.newrun5.create_cur_ver2.repository.DataItemRepository;
import com.newrun5.create_cur_ver2.component.OpenAIChatbotComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatbotService {

    @Autowired
    private DataItemRepository dataItemRepository;

    @Autowired
    private OpenAIChatbotService openAIChatbotService;


    // 사용자 입력을 처리하여 응답을 생성하는 메서드
    public String processUserMessage(String searchQuery, String userMessage) {
        // OpenAI를 사용해 입력을 파싱하고 자연스러운 응답을 생성
        String botResponse = openAIChatbotService.generateCurriculum(searchQuery, userMessage);

        return botResponse;
    }

//    public String processUserMessage(String searchQuery, String userMessage) {
//        // 검색어를 필터링하여 "배우고 싶은 대상"만 추출하는 로직
//        String topic = extractTopic(searchQuery);
//
//        // MongoDB에서 검색어와 관련된 데이터를 가져옵니다.
//        List<DataItem> dataItems = dataItemRepository.findAll();
//
//        // 사용자 이유에 맞춰 데이터를 필터링합니다.
//        List<String> filteredData = filterDataBasedOnReason(dataItems, userMessage, topic);
//
//        // 질문 5개를 생성합니다.
//        List<String> questions = generateQuestions(filteredData);
//
//        // OpenAI API로 커리큘럼 생성
//        return openAIChatbotComponent.getChatResponse(topic, userMessage, questions);
//    }

    // 검색어에서 배우고 싶은 대상을 필터링하는 메서드
    private String extractTopic(String searchQuery) {
        // 간단한 예로, "배우고 싶어" 등의 단어를 제외하고 대상을 추출하는 로직을 구현
        if (searchQuery.contains("를 배우고 싶어")) {
            return searchQuery.split("를 배우고 싶어")[0].trim();
        }
        return searchQuery;
    }

    // 데이터 필터링 메서드 (사용자 이유 기반)
    private List<String> filterDataBasedOnReason(List<DataItem> dataItems, String userReason, String topic) {
        return dataItems.stream()
                .filter(item -> item.getSnippet().contains(userReason) && item.getTitle().contains(topic))
                .map(DataItem::getTitle)
                .collect(Collectors.toList());
    }

    // 수준별 질문 생성 (5개 질문 생성)
    private List<String> generateQuestions(List<String> filteredData) {
        return filteredData.stream()
                .limit(5) // 최대 5개의 질문을 생성
                .map(data -> "이 항목에 대해 알고 있나요?: " + data)
                .collect(Collectors.toList());
    }
}
