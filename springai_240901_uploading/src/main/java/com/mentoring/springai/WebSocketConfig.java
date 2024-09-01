package com.mentoring.springai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

// 실제 프로젝트 패키지명으로 변경 필요
import com.mentoring.springai.websocket.ChatbotWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer
{

  @Autowired
  ChatbotWebSocketHandler handler;

  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
  {
    registry.addHandler(handler, "/ws-chat").setAllowedOrigins("*");
  }

}