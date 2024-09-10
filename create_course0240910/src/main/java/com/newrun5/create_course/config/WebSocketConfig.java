package com.newrun5.create_course.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.newrun5.create_course.websocket.ChatbotWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatbotWebSocketHandler chatbotWebSocketHandler;

    public WebSocketConfig(ChatbotWebSocketHandler chatbotWebSocketHandler) {
        this.chatbotWebSocketHandler = chatbotWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatbotWebSocketHandler, "/ws-chat").setAllowedOrigins("*");
    }
}

