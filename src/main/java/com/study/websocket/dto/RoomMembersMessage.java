package com.study.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 방 접속자 목록을 브로커가 브라우저로 밀어주는 메세지
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomMembersMessage {
    private String roomId;
    private List<String> members;
    private int count;
    private String sentAt;
}
