package com.study.websocket.controller;

import com.study.websocket.dto.ChatMessageResponse;
import com.study.websocket.dto.NotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.time.LocalDateTime;

// 여기서 컨트롤러 메서드의 리턴값 "나에게 돌아오는 응답"이 x
// 리턴값은 브로커에게 넘어감
@Controller
@RequiredArgsConstructor
public class ChatController {

    // 클라이언트는 localhost:8080/app/chat url로 stomp 메세지를 전송해야함
    // SendTo는 /topic/chat을 구독한다면 리턴한 데이터를 메세지로 받아 볼 수 있음 
    @MessageMapping("/chat")
    @SendTo("/topic/chat")
    public ChatMessageResponse sendAll(
            NotificationRequest request,
            Principal principal
    ) {
        return new ChatMessageResponse(
                principal.getName(),
                request.getMessage(),
                LocalDateTime.now().toString()
        );
    }

}
