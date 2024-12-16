package com.test.book.configuration;

import com.test.book.component.JwtTokenProvider;
import lombok.RequiredArgsConstructor;


/**
 * 스프링 시큐리티 관련해서 환경 설정을 진행하는 클래스
 */
//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true) // @Secured 활성화
@RequiredArgsConstructor
public class CustomSecurityConfiguration {

//    private final StringRedisTemplate stringRedisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
//    private final CustomUserDetailsService customUserDetailsService;
//    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            // 서버에 인증정보를 저장하지 않기에 csrf를 사용하지 않는다.
//                .csrf().disable()
//                .httpBasic().disable() // Basic 기반(username, password) 로그인 사용 안함
//                .formLogin().disable() // form 기반 로그인 사용 안함
//                // Session 기반의 인증기반을 사용하지 않고 추후 JWT를 이용하여서 인증 예정
//                // No session will be created of used by spring security
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//
//                .and()
//                .addFilterBefore(new CustomJWTAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
//                .authorizeRequests()
//                .antMatchers("/api/users/login", "/api/users/logout", "/api/users/signup").permitAll()
//                .anyRequest().authenticated(); //Authentication 필요한 주소
//        // 최종 구성한 값을 사용함.
//        return http.build();
//    }

//    @Bean
//    public CustomUsernamePasswordAuthenticationFilter authFilter() {
//        CustomUsernamePasswordAuthenticationFilter filter = new CustomUsernamePasswordAuthenticationFilter(stringRedisTemplate, "/api/users/login");
//        filter.setAuthenticationManager(authenticationManager());
//        return filter;
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Bean
//    public JwtTokenProvider authenticationProvider() {
//        return new JwtTokenProvider();
//    }
//
//    @Bean
//    public CustomJWTAuthenticationFilter jwtAuthenticationFilter() {
//        return new CustomJWTAuthenticationFilter(stringRedisTemplate);
//    }
//
//    // 비밀번호 암호화 클래스
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
