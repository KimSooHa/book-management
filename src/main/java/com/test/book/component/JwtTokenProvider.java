package com.test.book.component;

import com.test.book.dto.TokenDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.regex.Pattern;

import static com.test.book.component.JwtProperties.BEARER_PREFIX;

/**
 * 유저 정보로 jwt access/refresh 토큰 생성 및 재발급 + 토큰으로부터 유저 정보 받아옴
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

//    @Autowired
//    BCryptPasswordEncoder passwordEncoder;
//    private final RedisTokenUtils redisTokenUtils;

//    private final UserDetailsService userDetailsService;


    /**
     * secret key hashing
     * @return
     */
    private Key getSigninKey() {
        byte[] keyBytes = JwtProperties.SECRET.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDTO createToken(String email, String authorities) {
        long now = System.currentTimeMillis();
        String accessToken = createAccessToken(email, authorities, now);

        log.info("accessToken :: {}", accessToken);

        String refreshToken = createRefreshToken(email, authorities, now);

        log.info("token dto generated !!");

        return new TokenDTO(accessToken, refreshToken);
    }

    /**
     * access token create
     * @param email
     * @param authorities
     * @param now
     * @return
     */
    public String createAccessToken(String email, String authorities, long now) {
        return Jwts.builder()
                .setHeaderParam(JwtProperties.TYPE, JwtProperties.TYPE_VALUE)
                .setHeaderParam(JwtProperties.ALGORITHM, JwtProperties.ALGORITHM_VALUE)
                .setExpiration(new Date(now + JwtProperties.ACCESS_TOKEN_TTL))
                .setSubject("access-token")
                .claim(JwtProperties.USER_ID, email)
                .claim(JwtProperties.ROLE, authorities)
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * refresh token create
     * @param email
     * @param authorities
     * @param now
     * @return
     */
    private String createRefreshToken(String email, String authorities, long now) {
        return Jwts.builder()
                .setHeaderParam(JwtProperties.TYPE, JwtProperties.TYPE_VALUE)
                .setHeaderParam(JwtProperties.ALGORITHM, JwtProperties.ALGORITHM_VALUE)
                .setExpiration(new Date(now + JwtProperties.ACCESS_TOKEN_TTL))
                .setSubject("refresh-token")
                .signWith(getSigninKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * 사용자의 인증을 처리하는 메서드
     * 적절한 인증인지 판별(authenticate())
     * @param authentication
     * @return 인증 성공 시, 사용자 정보와 권한을 포함한 새로운 인증 객체
     * @throws AuthenticationException 인증 실패 또는 예외 발생 시
     */
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        // 사용자의 인증을 처리하고 성공하면 새로운 인증 객체를 반환한다.
//        CustomUserDetailsService customUserDetailsService = new CustomUserDetailsService();
//
//        String id = (String) authentication.getPrincipal();
//        String pwd = (String) authentication.getCredentials();
//
//        // id 유효성 체크
//        if (!validateLogin(id, pwd)) {
//            return null;
//        }
//
//        id = id.trim();
//        pwd = pwd.trim();
//
//        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(id);
//
//        // 새로운 인증 객체 반환
//        if (passwordEncoder.matches(pwd, userDetails.getPassword())) {
//            return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword());
//        }
//
//        // 실패할 경우 AuthenticationException을 throw 하거나 null을 반환할 수 있다.
//        return null;
//    }
//
//    /**
//     * 검증할 Authentication을 설정(supports())
//     * 이 AuthentivationProvider가 지원하는 인증 토큰 클래스를 지정한다.
//     * @param authentication 인증 토큰 클래스
//     * @return 이 AuthentivationProvider가 해당 인증 토큰 클래스를 지원하면 true, 아니면 false 반환
//     */
//    @Override
//    public boolean supports(Class<?> authentication) {
//        // 이 AuthenticationProvider 가 어떤 인증 토큰 클래스를 지원할지 지정한다.
//        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
//    }
    
    // 유효성 체크
    private boolean validateLogin(String id, String pwd) {
        if (null == id || id.trim().isEmpty()) {
            return false;
        }
        if (!(Pattern.matches("^[0-9a-zA-Z가-힣]{2,15}$", id.trim()))) {
            return false;
        }
        if (null == pwd || pwd.trim().isEmpty()) {
            return false;
        }
        return true;
    }

//    public String generateToken(User user, Duration expiredAt) {
//        Date now = new Date();
//        return makeToken(new Date(now.getTime() + expiredAt.toMillis()), user);
//    }

//    private String makeToken(Date expiry, User user) {
//        Date now = new Date();
//
//        return Jwts.builder()
//                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
//                .setIssuer(jwtProperties.getIssuer())
//                .setIssuedAt(now)
//                .setExpiration(expiry)
//                .setSubject(user.getEmail())
//                .claim("id", user.getId())
//                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
//                .compact();
//    }

    /**
     * token validation 파싱 및 검증
     *
     * @param token
     * @return
     */
    public boolean validationToken(String token) {
        try {
            token = token.replace(BEARER_PREFIX, "");

            Jwts.parserBuilder()
                    .setSigningKey(getSigninKey()).build()
                    .parseClaimsJws(token); // 파싱 및 검증, 실패 시 error
            return true;
        } catch (SignatureException e) { // 시그니처 검증에 실패한 토큰
            log.error("Invalid JWT signature: {}", e.getMessage());
            return false;
        } catch (MalformedJwtException e) { // 전달되는 토큰의 값이 유효하지 않음(손상된 형태)
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) { // 지원하지 않는 토큰
            log.error("JWT token is unsupported: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
            return false;
        }
    }

    /**
     * token으로 Authentication(인증정보)을 가져옴
     * @param token
     * @return
     */
//    public Authentication getAuthentication(String token) {
//        // 비밀값으로 토큰을 복호화한 뒤 클레임에 있는 사용자 이메일인 token subject를 가져온다
//        Claims claims = getClaims(token);
//        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
//        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
//    }

    /**
     * Claim 을 가져옴
     *
     * @param token
     * @return
     */
    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigninKey()).build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // 만료되어도 claim return
            return e.getClaims();
        }
    }

    /**
     * token의 만료 시간
     *
     * @param token
     * @return
     */
    private long getExpireTime(String token) {
        return getClaims(token).getExpiration().getTime();
    }

    private boolean validationTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

//    public String getToken(String authorizationHeader) {
//        // JWT가 Bearer 시작한다면 Bearer 빼고 return
//        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER_PREFIX)) {
//            return authrizationHeader.substring(BEARER_PREFIX.length());
//        }
//        return null;
//    }

//    public void removeTokenFromRedis(String token) {
//        if (stringRedisTemplate.opsForValue().get(token) != null) {
//            // Refresh Token을 삭제
//            stringRedisTemplate.delete(token);
//        } else {
//            throw new IllegalArgumentException("refresh key에 대한 값이 없습니다.");
//        }
//    }
//
//    public Long getUserIdFromRedis(String token) {
//        Optional<Long> optionalValue = Optional.ofNullable(Long.valueOf(stringRedisTemplate.opsForValue().get(token)));
//        return optionalValue.orElseThrow(() -> new IllegalArgumentException("refresh key에 대한 값이 없습니다."));
//    }

}
