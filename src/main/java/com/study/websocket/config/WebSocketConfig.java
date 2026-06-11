package com.study.websocket.config;

import com.study.websocket.interceptor.JwtChannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP 웹소켓 기능을 설정할거야
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtChannelInterceptor jwtChannelInterceptor;

    // STOMP는 url 기반으로 발행/구독 수행
    // 메세지가 다니는 "주소 규칙"을 정한다
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // /topic or /queue로 시작하는 모든 url은 브로커가 받는다.
        // ex) /topic/notification
        // 실무에서는 실제 외부 브로커소프트웨어를 등록
        
        // 구독받을 url을 설정
        // topic -> 공지사항 queue -> 1명 특정해서 메세지
        registry.enableSimpleBroker("/topic", "/queue", "/study");

        /*
            **중요
            클라이언트가 메세지를 보낼때 /user -> "사용자별 주소 변환" 전용으로 예약됨
        */
        
        
        // app으로 들어온 메세지는 @MessageMapping 컨트롤러로 가게끔설정
        registry.setApplicationDestinationPrefixes("/app");
    }

    // 브라우저가 처음 연결할 입구 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // 최초 웹소켓이 HTTP 연결할 주소
                .setAllowedOriginPatterns("*")
                .withSockJS(); // 옛날브라우저는 웹소켓 지원x
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtChannelInterceptor);
    }
}
