package com.study.websocket.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReplyEvent {
    private String commentAuthor; // 원댓글 작성자
    private String replier; // 대댓 작성자
    private String body; // 대댓 내용
}
