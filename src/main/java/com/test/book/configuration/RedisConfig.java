package com.test.book.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/************
 * @info : Redis Repository Config 클래스
 * @name : RedisConfig
 * @version : 1.0.0
 * @Description : Lettuce 사용(비동기 요청 처리), RedisRepository 방식.
 ************/
@Getter
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories // Redis Repository 활성화
public class RedisConfig {

        @Value("${spring.cache.redis.host}")
        private String host;

        @Value("${spring.cache.redis.port}")
        private int port;

        /**
         * 내장 혹은 외부의 Redis를 연결
         */
        @Bean
        public RedisConnectionFactory redisConnectionFactory(){
            return new LettuceConnectionFactory(host, port);
        }

        /**
         * RedisConnection에서 넘겨준 byte 값 객체 직렬화
         */
        @Bean
        public RedisTemplate<?,?> redisTemplate(){
            RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory());
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            return redisTemplate;
        }
}
