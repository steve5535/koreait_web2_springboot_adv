package com.study.websocket.controller;

import com.study.websocket.dto.TokenRequest;
import com.study.websocket.dto.TokenResponse;
import com.study.websocket.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final JwtUtil jwtUtil;

    @PostMapping("/auth/token")
    public TokenResponse token(@RequestBody TokenRequest req) {
        String username = req.getUsername();
        // ResponseStatusException -> 스프링프레임워크가 응답을 해줌
        // @ExceptionHandler가 없어도 됨
        if (username == null || username.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username은 필수입니다");
        }
        return new TokenResponse(jwtUtil.createToken(username.trim()));
    }
}
