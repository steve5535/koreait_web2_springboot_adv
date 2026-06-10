package com.study.websocket.service;

import com.study.websocket.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service @RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    // 공지 - 구족중인 모든 사람에게 메세지 전송
    public void broadcast(String message) {
        NotificationMessage payload = new NotificationMessage(
                message,
                LocalDateTime.now().toString()
        );

        // /topic -> 브로커가 받는 url prefix
        // destination을 구독한 유저는 모두 메세지를 받을 수 있다
        messagingTemplate.convertAndSend("/topic/notifications", payload);
    }

    // 실습) study/noti 를 구독한 유저들에세 메세지를 발송하는
    // 주의) study/noti를 구독하게끔 설정변경 필요
    public void broadcastToStudy(String message) {
        NotificationMessage payload = new NotificationMessage(
                message,
                LocalDateTime.now().toString()
        );

        // 메세지를 /study/noti 발행
        messagingTemplate.convertAndSend("/study/noti", payload);
    }
}
