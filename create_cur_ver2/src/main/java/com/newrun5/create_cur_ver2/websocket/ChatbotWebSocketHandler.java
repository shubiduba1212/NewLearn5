package com.newrun5.create_cur_ver2.websocket;

import com.newrun5.create_cur_ver2.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.json.JSONObject;

@Component
public class ChatbotWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatbotService chatbotService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JSONObject jsonMessage = new JSONObject(payload);

        String searchQuery = jsonMessage.getString("searchQuery");
        String userMessage = jsonMessage.getString("userMessage");

        // 사용자 메시지 처리
        String response = chatbotService.processUserMessage(searchQuery, userMessage);

        // 서버 응답 전송
        session.sendMessage(new TextMessage(response));
    }
}
