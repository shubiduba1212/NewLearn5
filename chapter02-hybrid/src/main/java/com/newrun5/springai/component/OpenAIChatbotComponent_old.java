//package com.newrun5.springai.component;
//// 필요한 라이브러리와 클래스를 불러옵니다.
//
//import java.util.List;
//import java.util.Map;
//
//import com.newrun5.springai.MongoSearchService;
//import com.newrun5.springai.TestFunction;
//import org.springframework.ai.model.function.FunctionCallbackContext;//AI 모델의 기능을 사용할 때 필요한 설정을 담는 클래스
//import org.springframework.ai.openai.OpenAiChatModel;//OpenAI의 채팅 모델을 사용하는 클래스
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import lombok.extern.slf4j.Slf4j; // 로깅 기능을 제공하는 코드 - 프로그램이 실행되는 동안 정보를 기록
//
//@Component
//@Slf4j
//public class OpenAIChatbotComponent_old {
//    // Spring이 자동으로 주입해 주는 OpenAiChatModel 객체를 선언합니다.
//    @Autowired
//    OpenAiChatModel chatModel;
//
//    // Spring이 자동으로 주입해 주는 FunctionCallbackContext 객체를 선언합니다.
//    @Autowired
//    FunctionCallbackContext functionCallbackContext;
//
//    @Autowired
//    TestFunction testFunction;
//
//    @Autowired
//    private MongoSearchService mongoSearchService;
//
//    // 시스템 메시지와 사용자 메시지를 받아서 AI의 응답을 반환하는 메서드입니다.
//    public String getChatResponse(String systemMessage, String userMessage) {
//        // 시스템 메시지를 로그로 출력
//        log.info("System message: " + systemMessage);
//
//        // 사용자 메시지를 기반으로 검색 수행
//        List<Map<String, Object>> searchResults = mongoSearchService.vectorSearch(userMessage);
//
//        // 검색 결과를 가공하여 응답 메시지 생성
//        String responseMessage = processSearchResults(searchResults);
//
//        // 결과 메시지 반환
//        return responseMessage;
//    }
//
//    private String processSearchResults(List<Map<String, Object>> searchResults) {
//        // 검색 결과를 가공하는 로직
//        // 예: 결과를 문자열로 변환
//        if (searchResults.isEmpty()) {
//            return "검색 결과가 없습니다.";
//        } else {
//            // 결과를 문자열로 변환하여 반환
//            return searchResults.toString();
//        }
//    }
////    public List<Map<String, Object>> getSearchResults(String query) {
////        // TestFunction의 searchFunction 호출
////        return testFunction.searchFunction(query).getBody(); // ResponseEntity에서 body를 추출
////    }
////    public Message getChatResponse(String systemMessage, String userMessage)
////    {
////        Message msgSys = new SystemMessage(systemMessage);
////        Message msgUser = new UserMessage(userMessage);
////
////        List<Message> list = new ArrayList<>();
////        list.add(msgUser);
////        list.add(msgSys);
////
////        Prompt prompt = new Prompt(list, OpenAiChatOptions.builder().withFunction("searchFunction").build());
////
////        Message result = chatModel.call(prompt).getResult().getOutput();
////        return result;
////    }
////    public Message getChatResponse(String systemMessage, String userMessage) {
////        // 시스템 메시지를 SystemMessage 객체로 만듭니다.
////        Message msgSys = new SystemMessage(systemMessage);
////
////        // 사용자 메시지를 UserMessage 객체로 만듭니다.
////        Message msgUser = new UserMessage(userMessage);
////
////        // 메시지를 저장할 목록을 만듭니다.
////        List<Message> list = new ArrayList<>();
////
////        // 사용자 메시지를 목록에 추가합니다.
////        list.add(msgUser);
////
////        // 시스템 메시지를 목록에 추가합니다.
////        list.add(msgSys);
////
////        // 메시지 목록과 AI 설정을 사용하여 Prompt 객체를 만듭니다.
////        Prompt prompt = new Prompt(list, OpenAiChatOptions.builder().withFunction("testFunction").build());
////
////        // chatModel 객체를 사용하여 AI에게 Prompt를 전달하고, AI의 응답을 받아옵니다.
////        Message result = chatModel.call(prompt).getResult().getOutput();
////
////        // AI의 응답을 반환합니다.
////        return result;
////    }
//}
