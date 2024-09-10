package com.newrun5.create_course.websocket;

import com.newrun5.create_course.component.OpenAIChatbotComponent;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.stereotype.Component;

@Component
public class ChatbotWebSocketHandler extends TextWebSocketHandler {

    private final OpenAIChatbotComponent openAIChatbotComponent;

    public ChatbotWebSocketHandler(OpenAIChatbotComponent openAIChatbotComponent) {
        this.openAIChatbotComponent = openAIChatbotComponent;
    }

    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        // 메시지 내용에서 주제, 이유, 난이도를 추출하는 로직 필요
//        String[] parts = message.getPayload().split(",");
//        String topic = parts[0];
//        String reason = parts[1];
//        String difficulty = parts[2];
//
//        String response = openAIChatbotComponent.getChatResponse(topic, reason, difficulty);
//        session.sendMessage(new TextMessage(response));
//    }
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String userMessage = message.getPayload();

        // AI 챗봇에 메시지를 보내고 응답을 받습니다.
        String response = openAIChatbotComponent.getChatResponse("System message here", userMessage);

        // 응답을 클라이언트로 전송합니다.
        session.sendMessage(new TextMessage(response));
    }
}

