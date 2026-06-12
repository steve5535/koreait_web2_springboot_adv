package com.study.websocket.service;

import com.study.websocket.dto.ChatMessageResponse;
import com.study.websocket.dto.NotificationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessagingTemplate messagingTemplate;

    // 금칙어를 리스트로 가지면 어떤식으로 바꿔야할까요?
    // 원래라면 db에서 꺼내오는 리스트
    private static final List<String> BAD_WORDS = List.of("피카츄", "라이츄", "파이리", "꼬부기", "바보");

    public void sendFiltered(String sender, String message) {
        String originalMessage = message == null ? "" : message;

        String filteredMessage = originalMessage;
        boolean hasBadword = false;
        for (String word : BAD_WORDS) {
            if (filteredMessage.contains(word)) {
                hasBadword = true;
                filteredMessage = filteredMessage.replace(word, "*".repeat(word.length()));
            }
        }

        String sentAt = LocalDateTime.now().toString();

        ChatMessageResponse chatResponse = new ChatMessageResponse(sender, filteredMessage, sentAt);

        // 채팅방 사람들한테는 마스킹된 메세지 보낸다
        messagingTemplate.convertAndSend("/topic/chat", chatResponse);

        // 금칙어를 보낸사람(sender에게) 개인 경고 알림
        // 같은 메세지입력에서 전체 & 개인알림 동시에 필요 -> @SendTo로는 힘들다
        if (hasBadword) {
            NotificationMessage warning = new NotificationMessage(
                    "금칙어가 포함되어 있습니다",
                    sentAt
            );
            messagingTemplate.convertAndSendToUser(sender, "/queue/notifications", warning);
        }

    }

}
