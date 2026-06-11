package com.study.websocket.service;

import com.study.websocket.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service @RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;

    // 공지 - 구독중인 모든 사람에게 메세지 전송
    public void broadcast(String message) {
        NotificationMessage payload = new NotificationMessage(
                message,
                LocalDateTime.now().toString()
        );

        // /topic -> 브로커가 받는 url prefix
        // destination을 구독한 유저는 모두 메세지를 받을 수 있다
        messagingTemplate.convertAndSend("/topic/notifications", payload);
    }

    public void broadcastToStudy(String message) {
        NotificationMessage payload = new NotificationMessage(
                message,
                LocalDateTime.now().toString()
        );

        // 메세지를 /study/noti 발행!
        messagingTemplate.convertAndSend("/study/noti", payload);
    }

    public void sendToUser(String username, String message) {
        NotificationMessage payload = new NotificationMessage(
                message,
                LocalDateTime.now().toString()
        );

        // user/queue/notifications로 메세지를 발행하겠다
        // -> username의 연결에게만 메세지가 발행됨
        log.info("username={}", username);
        messagingTemplate.convertAndSendToUser(username, "/queue/notifications", payload);
    }

}



