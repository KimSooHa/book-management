package com.test.book.filter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    AuthenticationManager authenticationManager;

    private final StringRedisTemplate stringRedisTemplate;


    public CustomUsernamePasswordAuthenticationFilter(StringRedisTemplate stringRedisTemplate, String filterProcessesUrl) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
}
