package com.study.websocket.interceptor;

import com.study.websocket.jwt.JwtUtil;
import com.study.websocket.jwt.StompPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

/*
    웹소켓은 HTTP 프로토콜 사용 x -> filter 안탐
    -> 별개로 채널을 만들어서 jwt를 추출해야 함
    stomp 프로토콜도 헤더가 존재함.

*/
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {
    private final JwtUtil jwtUtil;

    // 들어오는 모든 stomp 메세지마다 호출되는 메서드
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        /*
            연결시 CONNECT
            구독할때 SUBSCRIBE
            메세지 보낼때 SEND
            연결 종료 DISCONNECT
        */
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            setUserFromToken(accessor);
        }

        return message;
    }

    // CONNECT 헤더의 토큰 검증, username 추출
    private void setUserFromToken(StompHeaderAccessor accessor) {
        try {
            String header = accessor.getFirstNativeHeader("Authorization");
            String token = jwtUtil.resolveToken(header);
            String username = jwtUtil.validateTokenAndGetUsername(token);

            // 이 연결의 주인(principal)을 등록 -> 이후에 어디서든 이 이름으로 연결을 찾을 수 있음
            // 토큰 subject <--> Principal.getName() <--> sendToUser() 대상
            accessor.setUser(new StompPrincipal(username));
        } catch (RuntimeException e) {
            // jwt 관련 에러, 네트워크 에러 등
            throw new MessagingException("웹소켓 토큰 인증 실패: " + e.getMessage(), e);
            // 에러응답이 아닌 프레임워크가 에러 메세지를 보냄
        }
    }

    
    
}
