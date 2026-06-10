package com.study.websocket.jwt;


/*
    사용자 식별 - 스프링 시큐리티를 사용할 경우
    1) 세션 id를 가져와서 식별
    2) jwt 토큰으로 식별
 */

import lombok.RequiredArgsConstructor;

import javax.security.auth.Subject;
import java.security.Principal;

// Authentication 대응
@RequiredArgsConstructor
public class StompPrincipal implements Principal {
    private final String name;
    @Override
    public String getName() {
        return "";
    }
}
