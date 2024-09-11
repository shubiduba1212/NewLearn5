package com.newrun5.create_cur_ver2.service;

import com.newrun5.create_cur_ver2.component.OpenAIChatbotComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIChatbotService {

    private final MongoSearchService mongoSearchService;
    private final OpenAIChatbotComponent openAIChatbotComponent;

    @Autowired
    public OpenAIChatbotService(MongoSearchService mongoSearchService, OpenAIChatbotComponent openAIChatbotComponent) {
        this.mongoSearchService = mongoSearchService;
        this.openAIChatbotComponent = openAIChatbotComponent;
    }

    // 커리큘럼 생성 및 OpenAI API 호출 관리 메서드
    public String generateCurriculum(String searchQuery, String userMessage) {
        String promptText = "";

        // MongoDB에서 검색어와 관련된 데이터를 가져오기 (hybridSearch 사용)
        List<Map<String, Object>> relevantData = mongoSearchService.hybridSearch(searchQuery);

        // 검색 결과 처리
        if (relevantData == null || relevantData.isEmpty()) {
            promptText = "관련 데이터를 찾지 못했습니다. 학습 가능한 시간을 알려주세요. 하루에 몇 시간, 일주일에 며칠 정도 학습이 가능하신가요?";
        } else {
            promptText = "관련 데이터를 찾았습니다! 학습 가능한 시간을 알려주세요. 하루에 몇 시간, 일주일에 며칠 정도 학습이 가능하신가요?";
            String firstCurriculum = generateCurriculumFromSearchResults(relevantData);
            promptText += "\n생성된 1회차 커리큘럼:\n" + firstCurriculum;
        }

        // 사용자 메시지와 함께 OpenAI API 호출 (OpenAIChatbotComponent를 통해)
        return openAIChatbotComponent.callOpenAiChat(promptText + "\n" + userMessage);
    }

    // 검색 결과를 바탕으로 1회차 커리큘럼을 생성하는 메서드
    private String generateCurriculumFromSearchResults(List<Map<String, Object>> searchResults) {
        StringBuilder curriculum = new StringBuilder();
        curriculum.append("1회차 커리큘럼:\n");

        for (Map<String, Object> result : searchResults) {
            curriculum.append("제목: ").append(result.get("title")).append("\n");
            curriculum.append("내용: ").append(result.get("content")).append("\n");
        }

        return curriculum.toString();
    }
}
