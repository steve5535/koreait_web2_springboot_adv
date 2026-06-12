package com.study.websocket.service;

import com.study.websocket.dto.ChatMessageResponse;
import com.study.websocket.dto.RoomMembersMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private static final String SYSTEM_SENDER = "SYSTEM";
    private final SimpMessagingTemplate messagingTemplate;

    // room을 기억할건데, roomId 기준으로 기억
    // member들을 기억
    private final Map<String, List<String>> roomMembers = new LinkedHashMap<>();
    // 순서가 중요할때는 LinkedHashMap 사용
    // 정렬이 되는 HashMap이다

    public void join(String roomId, String username) {
        boolean firstJoin = addMember(roomId, username);
        if (firstJoin) {
            sendSystemMessage(roomId, username + "님이 입장했습니다.");
        }
        sendMembers(roomId);
    }

    // leave를 완성시키기
    public void leave(String roomId, String username) {
        boolean left = removeMember(roomId, username);
        if (left) {
            sendSystemMessage(roomId, username + "님이 퇴장했습니다");
        }
        sendMembers(roomId);
    }

    public void sendMembers(String roomId) {
        List<String> members = getMembers(roomId);
        RoomMembersMessage response = new RoomMembersMessage(
                roomId,
                members,
                members.size(),
                LocalDateTime.now().toString()
        );
        // /topic/rooms/{roomId}/members
        String url = "/topic/rooms/" + roomId + "/members";
        messagingTemplate.convertAndSend(url, response);
    }

    public List<String> getMembers(String roomId) {
        List<String> members = roomMembers.get(roomId);
        if (members == null) {
            return List.of();
        }
        // 복사해서 전달 - 리턴받은 곳에서 원본 수정할 수 없도록
        return new ArrayList<>(members);
    }


    // room에 멤버등록
    private boolean addMember(String roomId, String username) {
        // key가 있으면 get해오고, 없으면 새로 만들어라
        List<String> members = roomMembers.computeIfAbsent(roomId, n -> new ArrayList<>());
        if (members.contains(username)) {
            return false;
        }
        members.add(username);
        return true;
    }

    // room에 멤버삭제
    private boolean removeMember(String roomId, String username) {
        List<String> members = roomMembers.get(roomId);
        if (members == null) {
            return false;
        }
        boolean isSuccess = members.remove(username);
        if (!isSuccess) {
            return false;
        }
        return isSuccess;
    }

    private void sendSystemMessage(String roomId, String message) {
        ChatMessageResponse response = new ChatMessageResponse(
                SYSTEM_SENDER,
                message,
                LocalDateTime.now().toString()
        );
        // topic/rooms/{roomId}/chat
        String url = "/topic/rooms/" + roomId + "/chat";
        messagingTemplate.convertAndSend(url, response);
    }

}
