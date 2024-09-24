package com.newrun5.springai.component;

import groovy.util.logging.Slf4j;
import org.json.JSONObject;// JSON 파싱을 위해 필요
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.function.FunctionCallbackContext;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.newrun5.springai.MongoSearchServiceBasic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class OpenAIChatbotComponent {

    @Autowired
    OpenAiChatModel chatModel;
//    private MongoSearchServiceBasic mongoSearchService;

    @Autowired
    FunctionCallbackContext functionCallbackContext;

    public String getChatResponse(String systemMessage, String userMessage) {
            // SystemMessage와 UserMessage 객체를 생성합니다.
            SystemMessage msgSys = new SystemMessage(systemMessage);
            UserMessage msgUser = new UserMessage(userMessage);

            // 메시지 리스트를 생성합니다.
            List<Object> list = new ArrayList<>();
            list.add(msgUser);
            list.add(msgSys);

            // Prompt를 생성합니다.
            Prompt prompt = new Prompt(list.toString(), OpenAiChatOptions.builder()
                    .withFunction("hybridSearchFunction").build());

            // 모델 호출 및 결과 처리
            String result = chatModel.call(prompt).getResult().getOutput().getContent();
//            System.out.println("result: " + chatModel.call(prompt).getResult().getOutput().getContent());
            return result;
    }

//    public List<Map<String, Object>> getSearchResults(String query) {
//        // MongoSearchService의 vectorSearch 호출
//        return mongoSearchService.vectorSearch(query)
//                .getMappedResults() // AggregationResults에서 데이터 추출
//                .stream()
//                .map(doc -> (Map<String, Object>) doc)
//                .toList();
//    }
//    public String generateResponse(String query) {
//        List<Map<String, Object>> searchResults = mongoSearchService.vectorSearch(query);
//
//        // 결과를 바탕으로 응답 생성
//        String response = "✨궁금하신 내용을 알려드릴게요! " + query + ":";
//        if (searchResults.isEmpty()) {
//            response += " 죄송해요 ㅠㅠ 해당 내용은 찾을 수 없어요\uD83D\uDE4F";
//        } else {
//            // 검색 결과를 간단하게 요약
//            response += searchResults.stream()
//                    .map(result -> (String) result.get("content"))
//                    .limit(5) // 최대 5개의 결과를 표시
//                    .collect(Collectors.joining("\n- ", "\n- ", ""));
//        }
//        return response;
//    }
}

