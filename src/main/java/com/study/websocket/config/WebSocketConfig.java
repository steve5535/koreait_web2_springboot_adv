package com.study.websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP 웹소켓 기능을 설정할거야
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // STOMP는 url 기반으로 발행/구독 수행
    // 메세지가 다니는 "주소 규칙"을 정한다
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /topic or /queue로 시작하는 모든 url은 브로커가 받는다.
        // ex) /topic/notification
        // 실무에서는 실제 외부 브로커소프트웨어를 등록

        // 구독받을 url을 설정
        registry.enableSimpleBroker("/topic", "/queue", "/study");

        // app으로 들어온 메세지는 @MessageMapping 컨트롤러로 가게끔걸정
        registry.setApplicationDestinationPrefixes("/app");
    }

    // 브라우저가 처음 연결할 입구 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        WebSocketMessageBrokerConfigurer.super.registerStompEndpoints(registry);
        registry.addEndpoint("/ws") // 최초 웹소켓이 HTTP 연결할 주소
                .setAllowedOriginPatterns("*") // 실습이라 모든 출처(origin) open
                .withSockJS(); // 옛날브라우저는 웹소켓 지원x
    }
}
