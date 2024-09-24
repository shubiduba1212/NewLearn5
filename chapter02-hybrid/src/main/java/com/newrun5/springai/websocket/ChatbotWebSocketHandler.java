package com.newrun5.springai.websocket;

// 필요한 라이브러리와 클래스를 불러옵니다.
import org.apache.logging.log4j.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage; //WebSocket을 통해 전송되는 텍스트 메시지를 표현 - 클라이언트로부터 받은 메시지를 처리하고, 서버에서 클라이언트로 메시지를 보낼 수 있다.
import org.springframework.web.socket.WebSocketSession; //클라이언트와 서버 간의 WebSocket 연결을 관리하는 데 사용 - 세션을 통해 메시지를 전송하거나 상태를 확인 : 클라이언트와의 연결을 유지하고 메시지를 보내는 데 이 세션을 사용
import org.springframework.web.socket.handler.TextWebSocketHandler; //WebSocket 핸들러의 기본 구현을 제공하는 클래스 - 클라이언트로부터 텍스트 메시지를 수신하고, 응답을 전송하는 로직을 작성

// 실제 프로젝트 패키지명으로 변경 필요
import com.newrun5.springai.component.OpenAIChatbotComponent;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ChatbotWebSocketHandler extends TextWebSocketHandler
{
    // 시스템 메시지를 정의합니다. 이 메시지는 Chatbot의 역할을 설명합니다.
    String systemMessage = "사용자 질문에 답변하는 Chatbot Assistant입니다. 상황에 따라서 필요한 함수를 적절하게 호출합니다.";

    // Spring이 자동으로 주입해 주는 OpenAIChatbotComponent 객체를 선언합니다.
    @Autowired
    private OpenAIChatbotComponent openAIChatbotComponent;

    @Autowired
    private OpenAIChatbotComponent chatbotComponent;

    // 클라이언트로부터 텍스트 메시지를 받을 때 호출되는 메서드입니다.
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 클라이언트로부터 수신한 메시지
        String userMessage = message.getPayload();

        // 시스템 메시지 예시 (필요시 수정)
        String systemMessage = "System message here";

        // OpenAIChatbotComponent에서 응답을 생성
        String response = openAIChatbotComponent.getChatResponse(systemMessage, userMessage);

        // 클라이언트에게 응답 전송
        session.sendMessage(new TextMessage(response));
    }
//    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        String response = chatbotComponent.getChatResponse()Response(payload);
////        String response = chatbotComponent.generateResponse(payload);
//        session.sendMessage(new TextMessage(response));
//    }
//    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        log.info("Received message: " + payload);
//
//        // Chatbot 컴포넌트를 사용하여 응답 메시지를 생성합니다.
//        List<Map<String, Object>> searchResults = chatbotComponent.getSearchResults(payload);
//        String responseText = searchResults.isEmpty() ? "검색 결과가 없습니다." : searchResults.toString();
//
//        // 생성된 응답 메시지를 클라이언트에게 전송합니다.
//        session.sendMessage(new TextMessage(responseText));
//    }
//    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception
//    {
//        String payload = message.getPayload();
//        log.info("Received message: " + payload);
//
//        String response;
//        if (payload.equalsIgnoreCase("안녕")) {
//            response = "안녕하세요! 어떻게 도와드릴까요?";
//        } else {
//            try {
//                // Chatbot 컴포넌트를 사용하여 검색 결과를 생성합니다.
//            List<Map<String, Object>> searchResults = chatbotComponent.getSearchResults(payload);
////                response = msg.getContent();
//                response = searchResults.toString();
//            } catch (Exception e) {
//                log.error("Error during search", e);
//                response = "죄송합니다, 요청 처리 중 오류가 발생했습니다.";
//            }
//        }
//
//        // 응답 메시지를 클라이언트에게 전송합니다.
//        session.sendMessage(new TextMessage(response));
////        try {
////            // 클라이언트가 보낸 메시지를 가져옵니다.
////            String payload = message.getPayload();
////            // 받은 메시지를 로그에 기록합니다.
////            log.info("Received message: " + payload);
////
////            // Chatbot 컴포넌트를 사용하여 검색 결과를 생성합니다.
////            List<Map<String, Object>> searchResults = chatbotComponent.getSearchResults(payload);
////
////            // 검색 결과를 클라이언트에게 전송합니다.
////            String response = searchResults.toString();
////            session.sendMessage(new TextMessage(response));
////        } catch (Exception e) {
////            // 예외를 로그에 기록합니다.
////            log.error("Error handling message: ", e);
////            // 클라이언트에게 에러 메시지를 전송합니다.
////            session.sendMessage(new TextMessage("Error processing your request."));
////        }
////        // 클라이언트가 보낸 메시지를 가져옵니다.
////        String payload = message.getPayload();
////        // 받은 메시지를 로그에 기록합니다.
////        log.info("Received message: " + payload);
////
////        // Chatbot 컴포넌트를 사용하여 검색 결과를 생성합니다.
////        List<Map<String, Object>> searchResults = chatbotComponent.getSearchResults(payload);
////
////        // 검색 결과를 클라이언트에게 전송합니다.
////        // 결과를 JSON 문자열로 변환하여 클라이언트에게 보냅니다.
////        String response = searchResults.toString();
////        session.sendMessage(new TextMessage(response));
//
////        // Chatbot 컴포넌트를 사용하여 응답 메시지를 생성합니다.
////        Message msg = chatbotComponent.getChatResponse(systemMessage, payload);
////
////        // 생성된 응답 메시지를 클라이언트에게 전송합니다.
////        session.sendMessage(new TextMessage(msg.getContent()));
//    }

    // WebSocket 연결이 성립된 후 호출되는 메서드입니다.
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception
    {
        // 연결이 성립되었음을 로그에 기록합니다.
        log.info("Connection established with session: " + session.getId());
    }
}

