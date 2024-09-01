package com.mentoring.springai.websocket;

import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// 실제 프로젝트 패키지명으로 변경 필요
import com.mentoring.springai.component.OpenAIChatbotComponent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChatbotWebSocketHandler extends TextWebSocketHandler
{
  String systemMessage = "사용자 질문에 답변하는 Chatbot Assistant입니다. 상황에 따라서 필요한 함수를 적절하게 호출합니다.";

  @Autowired
  OpenAIChatbotComponent chatbotComponent;

  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception
  {

    // 클라이언트로부터 받은 메시지를 처리하는 로직을 작성
    String payload = message.getPayload();
    log.info("Received message: " + payload);

    Message msg = chatbotComponent.getChatResponse(systemMessage, payload);

    // 클라이언트에 응답 메시지 전송
    session.sendMessage(new TextMessage(msg.getContent()));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception
  {
    // WebSocket 연결이 성립된 후의 로직 작성
    log.info("Connection established with session: " + session.getId());
  }
}