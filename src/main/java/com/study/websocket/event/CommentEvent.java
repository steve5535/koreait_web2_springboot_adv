package com.study.websocket.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 댓글이 달렸다 라는 사실을 담은 객체
@AllArgsConstructor
@Getter
public class CommentEvent {
    private String postAuthor; // 글 작성자
    private String commenter; // 댓글 쓴 사람
    private String body; // 댓글내용
}
