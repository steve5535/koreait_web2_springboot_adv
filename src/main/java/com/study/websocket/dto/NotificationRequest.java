package com.study.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// HTTP 요청으로 공지발송 -> noti msg 발행
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String message;
}
