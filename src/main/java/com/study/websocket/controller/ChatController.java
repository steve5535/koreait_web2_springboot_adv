package com.study.websocket.controller;

import com.study.websocket.dto.ChatMessageResponse;
import com.study.websocket.dto.MessageRequest;
import com.study.websocket.dto.NotificationRequest;
import com.study.websocket.service.ChatRoomService;
import com.study.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

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

    // 서로다른 목적지에 서로다른 메세지를 보내는 경우
    // @SendTo으로는 한계가 존재
    // -> simpMessagingTemplate 사용
    @MessageMapping("/chat/filter")
    public void sendFiltered(MessageRequest request, Principal principal) {
        chatService.sendFiltered(principal.getName(), request.getMessage());
    }

    /*
        브라우저(클라이언트) 입장에서 할일
        입장(구독) - /topic/chat/{roomId}
        보내기(발행) - /app/chat/{roomId}
     */
    /*
        서버 입장에서 할일
        /app/chat/{roomId} 들어온 데이터를 /topic/chat/{roomId}로 밀어주기
     */
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatMessageResponse sendRoom(
            @DestinationVariable String roomId, // stomp에서의 pathvariable
            MessageRequest request, Principal principal
    ) {
        // roomId는 목록에서 조회해온 값이 x, STOMP 주소에서 꺼내온 값
        return new ChatMessageResponse(
                principal.getName(), request.getMessage(), LocalDateTime.now().toString()
        );
    }

    // 방상태를 포함한 채팅방 채팅
    @MessageMapping("/rooms/{roomId}/chat")
    @SendTo("/topic/rooms/{roomId}/chat")
    public ChatMessageResponse sendJoinedRoom(
            @DestinationVariable String roomId,
            MessageRequest request, Principal principal
    ) {
        return new ChatMessageResponse(
                principal.getName(), request.getMessage(), LocalDateTime.now().toString()
        );
    }

    @MessageMapping("/rooms/{roomId}/join")
    public void JoinRoom(
            @DestinationVariable String roomId, Principal principal
    ) {
        chatRoomService.join(roomId, principal.getName());
    }

    @MessageMapping("/rooms/{roomId}/leave")
    public void leaveRoom(
            @DestinationVariable String roomId, Principal principal
    ) {
        chatRoomService.leave(roomId, principal.getName());
    }

    @MessageMapping("/rooms/{roomId}/members")
    public void sendRoomMembers(
            @DestinationVariable String roomId
    ) {
        chatRoomService.sendMembers(roomId);
    }

}
