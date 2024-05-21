package com.test.book.redis.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisTokenUtils {
    private final StringRedisTemplate stringRedisTemplate;

    private final String ACCESS_TOKEN_KEY = "access_token:";
    private final String REFRESH_TOKEN_KEY = "refresh_token:";


    public RedisTokenUtils(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    
    // 엑세스 토큰 저장
    public void setAccessToken(String accessToken, Integer accessExpiredAt, int userIdx) {
        RedisUtils redisUtils = new RedisUtils(stringRedisTemplate);
        String key = ACCESS_TOKEN_KEY + userIdx;
        redisUtils.setData(key, accessToken);
        redisUtils.setDataExpireByMillis(key, accessToken, accessExpiredAt);
    }

    // 리프레시 토큰 저장
    public void setRefreshToken(String refreshToken, Integer refreshExpiredAt, int userIdx) {
        RedisUtils redisUtils = new RedisUtils(stringRedisTemplate);
        String key = REFRESH_TOKEN_KEY + userIdx;
        redisUtils.setData(key, refreshToken);
        redisUtils.setDataExpireByMillis(key, refreshToken, refreshExpiredAt);
    }

    // 레디스 : userIdx로 엑세스 토큰 가져오기
    public String getAccessToken(Integer userIdx) {

    }
}
