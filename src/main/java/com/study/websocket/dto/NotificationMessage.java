package com.study.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 서버가 일방적으로 밀어주는 알림 한 건
// 서버가 만들어서 내보내기만 할 거임
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage {
    private String message; // 내용
    private String sentAt; // 보낸 시각
}
