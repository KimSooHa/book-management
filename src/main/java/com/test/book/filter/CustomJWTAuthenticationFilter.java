package com.test.book.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomJWTAuthenticationFilter extends OncePerRequestFilter {

    @Value("${spring.profiles.active}")
    private String profile;

    private final StringRedisTemplate stringRedisTemplate;

    static final String HEADER_STRING = "Authorization";
    static final String HEADER_BEARER = "Bearer";

    public CustomJWTAuthenticationFilter(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 운영계일 때
        if ("real".equals(profile)) {
            doTokenVerify(request, response, filterChain);
        } else {
            doTokenVerify(request, response, filterChain);
        }
    }

    private void doTokenVerify(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {

    }
}
