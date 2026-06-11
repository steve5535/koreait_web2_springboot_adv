package com.study.websocket.controller;

// 특정 대상에게 메세지를 내려주는 웹소켓 방식을 사용하여
// 내 게시글에 댓글이 달리면 알림이 오는 서비스

import com.study.websocket.dto.CommentRequest;
import com.study.websocket.dto.ReplyRequest;
import com.study.websocket.event.CommentEvent;
import com.study.websocket.event.ReplyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    // 스프링에서 미리 만들어놓은 이벤트 발행 객체
    // 이벤트를 발행하면 그 타입을 듣고있는 이벤트리스너들이 호출됨
    private final ApplicationEventPublisher publisher;

    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.OK)
    public void addComment(@RequestBody CommentRequest req) {
        // 진짜 서비스라면 여기서 댓글을 db에 저장하는 코드(서비스메서드)
        
        // 웹소켓 + HTTP를 섞는 방식
        // 분리하는 방식 -> 퍼블리셔 + 리스너
        // 퍼블리셔가 발행하는 타입을 듣는 리스너가 존재하면
        // 매칭되서 호출되는 방식
        publisher.publishEvent(
                new CommentEvent(req.getPostAuthor(), req.getCommenter(), req.getBody())
        );
    }

    @PostMapping("/replies")
    @ResponseStatus(HttpStatus.OK)
    public void addReply(@RequestBody ReplyRequest request) {
        // 트랜잭션으로 대댓글 db 저장
        ReplyEvent e = new ReplyEvent(request.getCommentAuthor(), request.getReplier(), request.getBody());
        publisher.publishEvent(e);
    }

}
