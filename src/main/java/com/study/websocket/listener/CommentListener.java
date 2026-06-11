package com.study.websocket.listener;

import com.study.websocket.event.CommentEvent;
import com.study.websocket.event.ReplyEvent;
import com.study.websocket.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentListener {

    private final NotificationService notificationService;

    // 이 메서드가 CommentEvent를 구독한다 라는 의미
    // 메서드이름이 아니라 매개변수 타입이 구독 기준
    @EventListener
    public void onCommentCreated(CommentEvent e) {
        String msg = e.getCommenter() + "님이 회원님의 글에 댓글을 남겼습니다: " + e.getBody();
        notificationService.sendToUser(e.getPostAuthor(), msg);
    }

    @EventListener
    public void onReplyCreated(ReplyEvent e) {
        String msg = e.getReplier() + "님이 답글을 남겼습니다: " + e.getBody();
        notificationService.sendToUser(e.getCommentAuthor(), msg);
    }


}
