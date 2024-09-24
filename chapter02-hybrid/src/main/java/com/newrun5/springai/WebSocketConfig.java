package com.newrun5.springai;

// 필요한 라이브러리와 클래스를 불러옵니다.
import org.springframework.beans.factory.annotation.Autowired; // Spring에서 의존성 주입을 위해 사용합니다.
import org.springframework.context.annotation.Configuration; // Spring에서 이 클래스가 설정 클래스임을 나타냅니다.
import org.springframework.web.socket.config.annotation.EnableWebSocket; // WebSocket을 사용할 수 있게 해줍니다.
import org.springframework.web.socket.config.annotation.WebSocketConfigurer; // WebSocket 설정을 위한 인터페이스입니다.
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry; // WebSocket 핸들러를 등록하기 위해 사용합니다.

// 실제 프로젝트 패키지명으로 변경 필요
import com.newrun5.springai.websocket.ChatbotWebSocketHandler; // WebSocket 핸들러 클래스를 불러옵니다.

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer
{
    // Spring이 자동으로 주입해 주는 WebSocket 핸들러 객체를 선언합니다.
    @Autowired
    ChatbotWebSocketHandler handler;

    // WebSocket 핸들러를 등록하는 메서드입니다.
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry)
    {
        // 핸들러를 등록하고, "/ws-chat" 경로로 WebSocket 요청을 처리하도록 설정합니다.
        registry.addHandler(handler, "/ws-chat").setAllowedOrigins("*");
        // setAllowedOrigins("*")는 모든 출처의 요청을 허용합니다.
    }
}

