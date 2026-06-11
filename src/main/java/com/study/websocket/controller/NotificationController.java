package com.study.websocket.controller;

import com.study.websocket.dto.NotificationRequest;
import com.study.websocket.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    // 컨트롤러가 void이면 body가 없음
    @PostMapping("/notifications")
    @ResponseStatus(HttpStatus.OK) // 200
    public void send(@RequestBody NotificationRequest request) {
        log.info("request={}", request);
        notificationService.broadcast(request.getMessage());
    }

    @PostMapping("/notifications/study")
    @ResponseStatus(HttpStatus.OK)
    public void send2(@RequestBody NotificationRequest request) {
        log.info("request={}", request);
        notificationService.broadcastToStudy(request.getMessage());
    }

    @PostMapping("/notifications/users/{username}")
    @ResponseStatus(HttpStatus.OK)
    public void sendToUser(
            @PathVariable String username,
            @RequestBody NotificationRequest request
    ) {
        notificationService.sendToUser(username, request.getMessage());
    }


}










