package com.study.websocket.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyRequest { // 이벤트발행 + 리스너 두가지를 사용하여 study2.html가 작동하게
    private String commentAuthor; // 원댓글 작성자
    private String replier; // 대댓 작성자
    private String body; // 대댓 내용
}
