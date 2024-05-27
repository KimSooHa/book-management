package com.test.book.filter;


import com.test.book.component.JwtTokenProvider;
import io.jsonwebtoken.IncorrectClaimException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static com.test.book.component.JwtProperties.BEARER_PREFIX;

/**
 * request 앞단에 붙이는 filter. http request 에서 토큰을 받아와 정상 토큰일 경우 security context 에 저장
 * OncePerRequestFilter
 * 원래는 요청이 인증 후에 다른 서블릿으로 dispatch 되어질 경우, 다시 한번 필터를 통과해야 한다.
 * 이렇게 되면 이미 사용자 인증을 했음에도 불구하고 다시 한번 인증을 해야하는 자원 낭비가 이루어진다.
 * => 어느 서블릿 컨테이너에서나 요청 당 한번의 실행을 보장하는 OncePerRequestFilter 를 사용한다.(한번만 실행되기 때문에 여러번의 인증을 막을 수 있음)
 */
@Slf4j
@RequiredArgsConstructor
public class CustomJWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    static final String HEADER_STRING = "Authorization";
    static final String HEADER_BEARER = "Bearer";

    /**
     * requrst header에서 token을 꺼내고 유효성 검사 후 정보를 꺼내 Security Context 에 저장
     * 토큰 정보가 없거나 유효하지 않은 경우 정상적으로 수행되지 않음
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("OncePerRequestFilter in !!");

        // 요청 헤더의 Authorization 키의 값 조회
//        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String accessToken = getToken(request);

        try { // 정상 토큰인지 검사
            // 토큰 유효성 검사
            if (StringUtils.hasText(accessToken) && jwtTokenProvider.validationToken(accessToken)) {
                // 여기서 권한을 넣어줘야 Spring security가 권한 관리를 할 수 있다.
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("Save authentication in SecurityContextHolder");
            }
        } catch (IncorrectClaimException e) { // 잘못된 토큰일 경우
            SecurityContextHolder.clearContext();
            response.sendError(403);
        } catch (UsernameNotFoundException e) { // 회원을 찾을 수 없을 경우
            SecurityContextHolder.clearContext();
            log.debug("Can't find user.");
            response.sendError(403);
        }

        filterChain.doFilter(request, response);
    }

    
    // HTTP Request 헤더로부터 토큰 추출
    private String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
