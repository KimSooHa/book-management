package com.test.book.service;

import com.test.book.component.JwtProperties;
import com.test.book.component.JwtTokenProvider;
import com.test.book.domain.User;
import com.test.book.dto.LoginDTO;
import com.test.book.dto.TokenDTO;
import com.test.book.dto.UserRequestDto;
import com.test.book.redis.RedisUtils;
import com.test.book.repository.UserRepository;
import com.test.book.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisUtils redisUtils;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 회원 가입
     */
    @Transactional  // 변경해야 하기 때문에 읽기, 쓰기가 가능해야 함
    public Long join(UserRequestDto userDto) {
        User user = new User(userDto.getName(), userDto.getLoginId(), userDto.getPwd(), userDto.getEmail());
        validateDuplicateUser(user);    // 중복 회원 검증
        userRepository.save(user);
        return user.getId();
    }

    /*
        중복되는 사용자 체크
     */
    private void validateDuplicateUser(User user) {
        Optional<User> findMember = userRepository.findByLoginId(user.getLoginId());   // 로그인 아이디로 회원 찾기

        // 해당 아이디의 회원이 있으면
        findMember.ifPresent((m) -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
        Long emailCnt = countByEmail(user.getEmail());
        // 해당 이메일의 회원이 있으면
        if (emailCnt != 0) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /*
        이메일로 사용자 카운트
     */
    public Long countByEmail(String email) {
        return userRepository.countByEmail(email);
    }


    /**
     * login
     *
     * @param loginDTO
     * @return
     */
    public TokenDTO login(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getLoginId(), loginDTO.getPwd());
        AuthenticationManager authenticationManager = authenticationManagerBuilder.getObject();
        Authentication authentication = authenticationManager.authenticate(authenticationToken); // 인증

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String loginId = authentication.getName();
        String authorities = getAuthorities(authentication);

        return generateToken(loginId, authorities);
    }

    /**
     * Redis에서 RT가 있다면 DELETE, Token create & Redis에 RT 저장
     * @param loginId
     * @param authorities
     * @return
     */
    private TokenDTO generateToken(String loginId, String authorities) {
        // RT가 이미 있을 경우
        if (redisUtils.getData(JwtProperties.RT + loginId) != null) {
            redisUtils.deleteData(JwtProperties.RT + loginId);
        }

        TokenDTO tokenDTO = jwtTokenProvider.createToken(loginId, authorities);
        return tokenDTO;
    }

    /**
     * redis에 refreshToken 저장
     * @param principal
     * @param refreshToken
     */
    @Transactional
    private void saveRefreshToken(String principal, String refreshToken) {
        redisUtils.setDataExpireByMillis(JwtProperties.RT + principal, refreshToken, JwtProperties.REFRESH_TOKEN_TTL);
    }

    /**
     * 권한 이름 가져오기
     * @param authentication
     * @return
     */
    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    /**
     * logout
     * @param accessToken
     */
    public void logout(String accessToken) {
        accessToken = JwtUtils.getToken(accessToken);
        String principal = jwtTokenProvider.getAuthentication(accessToken).getName();

        // Redis에 저장되어 있는 RT 삭제
        if (redisUtils.getData(JwtProperties.RT + principal) != null) {
            redisUtils.deleteData(JwtProperties.RT + principal);
        }
    }
}
