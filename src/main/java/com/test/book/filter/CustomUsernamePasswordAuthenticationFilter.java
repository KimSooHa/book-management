//package com.test.book.filter;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
///**
// * [username, password] 를 이용해서 정상적인 로그인 여부를 검증
// * Authentication을 반환받고 security session에 저장한다.(권한 관리를 위해서 세션에 저장)
// */
//@Slf4j
//public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    @Autowired
//    AuthenticationManager authenticationManager;
//
//    private final StringRedisTemplate stringRedisTemplate;
//
//
//    public CustomUsernamePasswordAuthenticationFilter(StringRedisTemplate stringRedisTemplate, String filterProcessesUrl) {
//        this.stringRedisTemplate = stringRedisTemplate;
////        setFilterProcessesUrl(filterProcessesUrl);
//    }
//}
