package com.study.websocket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {
    private String postAuthor; // 글 작성자
    private String commenter; // 댓글 쓴 사람
    private String body; // 댓글내용
}
