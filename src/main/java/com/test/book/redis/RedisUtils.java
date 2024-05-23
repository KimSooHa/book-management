package com.test.book.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisUtils {
    private final StringRedisTemplate stringRedisTemplate;

    public String getData(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    public void setData(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    public void setDataExpireByMillis(String key, String value, long duration) { // 레디스 만료 기간 설정 밀리초 기준으로 변경
        Duration expireDuration = Duration.ofMillis(duration);
        stringRedisTemplate.opsForValue().set(key, value, expireDuration);
    }

    public void setDataExpireBySeconds(String key, String value, long duration) { // 레디스 만료 기간 설정 세컨드 기준으로 변경
        Duration expireDuration = Duration.ofSeconds(duration);
        stringRedisTemplate.opsForValue().set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }

    public boolean isKeyExpired(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

    // 레디스에 데이터 넣기 트랜잭션
    public void setDataWithTransaction(String key, String value) {
        try {
            stringRedisTemplate.multi(); // 트랜잭션 시작
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            valueOperations.set(key, value);
            stringRedisTemplate.exec(); // 트랜잭션 실행
        } catch (Exception e) { // 에러 전송
            e.printStackTrace();
        }
    }

}
