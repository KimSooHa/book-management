package com.test.book.configuration;

import com.sun.org.apache.xerces.internal.parsers.SecurityConfiguration;
import com.test.book.component.CustomUserDetailsService;
import com.test.book.filter.CustomUsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Arrays;


/**
 * 스프링 시큐리티 관련해서 환경 설정을 진행하는 클래스
 */
@Configuration
@EnableWebSecurity
public class CustomSecurityConfiguration extends SecurityConfiguration {

    private final StringRedisTemplate stringRedisTemplate;
    private final CustomUserDetailsService customUserDetailsService;


    public CustomSecurityConfiguration(StringRedisTemplate stringRedisTemplate, CustomUserDetailsService customUserDetailsService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.customUserDetailsService = customUserDetailsService;
    }

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // [STEP1] 서버에 인증정보를 저장하지 않기에 csrf를 사용하지 않는다.
                .csrf().disable()
                .antMatcher("/api/users")
                .authorizeRequests()
                .antMatchers("/api/users/login").permitAll()
                .and()
                .logout()
                .logoutUrl("/api/users/logout")
                .logoutSuccessHandler()
                .deleteCookies("JSESSIONID", "visit_cookie")
                .permitAll()
                .and()

                // [STEP3] Spring Security JWT Filter Load
//                .addFilterBefore(jwtAuthorizationFilter(), BasicAuthenticationFilter.class)

                // [STEP4] Session 기반의 인증기반을 사용하지 않고 추후 JWT를 이용하여서 인증 예정
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                // [STEP5] form 기반의 로그인에 대해 비 활성화하며 커스텀으로 구성한 필터를 사용한다.
                .formLogin().disable()

                // [STEP6] Spring Security Custom Filter Load - Form '인증'에 대해서 사용
                .addFilterBefore(authFilter(), CustomUsernamePasswordAuthenticationFilter.class);

        // [STEP7] 최종 구성한 값을 사용함.
        return http.build();
    }

    @Bean
    public CustomUsernamePasswordAuthenticationFilter authFilter() {
        CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter(stringRedisTemplate, "/api/users/login");
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean(name = "AuthenticationManagerBean")
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(authenticationProvider()));
    }

    @Bean
    public CustomAuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider()
    }


}
