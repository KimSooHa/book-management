package com.test.book.controller;

import com.google.common.net.HttpHeaders;
import com.test.book.component.JwtProperties;
import com.test.book.component.JwtTokenProvider;
import com.test.book.dto.LoginDTO;
import com.test.book.dto.TokenDTO;
import com.test.book.dto.UserRequestDto;
import com.test.book.dto.UserResponseDto;
import com.test.book.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;


    /**
     * 회원가입
     *
     * @param result
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity create(@RequestBody @Valid UserRequestDto userRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            log.info("errors={}", result);
            return ResponseEntity.badRequest().body("Invalid input: " + result.getAllErrors());
        }
        Long userId;
        try {
            userId = userService.join(userRequestDto);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        log.info("회원가입 성공!");
        return ResponseEntity.ok().body(new UserResponseDto("회원가입 성공!", userId));
    }

    /**
     * login api header : refresh-toke(RT) cookie, header : Authorization : Bearer + accessToken
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("/login")
//    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO loginDTO) {
//        log.info("login controller in!! {}", loginDTO);
//        TokenDTO tokenDTO = userService.login(loginDTO);
//
//        ResponseCookie httpCookie = ResponseCookie.from("refresh-token", tokenDTO.getRefreshToken())
//                .maxAge(JwtProperties.COOKIE_TTL)
//                .httpOnly(true) // client에서 script로 cookie 접근 제한
//                // .secure(true) // http가 아니면 쿠키 전송 안함
//                .build();
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
//                .header(HttpHeaders.AUTHORIZATION,
//                        JwtProperties.BEARER_PREFIX + tokenDTO.getAccessToken())
//                .build();
//    }


    /**
     * accessToken이 정상적이면 200, 아니면 401
     *
     * @param accessToken
     * @return
     */
    @GetMapping("/check/access-token")
    public ResponseEntity<Void> checkAccessToken(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String accessToken) {
        if (jwtTokenProvider.validationToken(accessToken)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * reissue : validate 요청으로부터 UNAUTHORIZED(401)을 반환받았다면,
     * 프론트에서 Cookie와 Header에 각각 RT와 AT를 요청으로 받아서 UserService.reissue를 통해 토큰 재발급을 진행한다.
     * 토큰 재발급이 성공한다면 login과 마찬가지로 응답 결과를 보내고,
     * 토큰 재발급이 실패했을 때(null을 반환받았을 때) Cookie에 담긴 RT를 삭제하고 재로그인을 유도한다.
     *
     * @param accessToken
     * @param refreshToken
     * @return
     */
//    @PostMapping("/reissue")
//    public ResponseEntity<?> reissue(
//            @RequestHeader(value = HttpHeaders.AUTHORIZATION) String accessToken,
//            @CookieValue("refresh-token") String refreshToken
//    ) {
//        TokenDTO tokenDTO = userService.reissue(accessToken, refreshToken);
//
//        if (tokenDTO != null) { // 토큰 재발급 성공
//            HttpCookie httpCookie = ResponseCookie.from("refresh-token", tokenDTO.getRefreshToken())
//                    .maxAge(JwtProperties.COOKIE_TTL)
//                    .httpOnly(true) // client에서 script로 cookie 접근 제한
//                    // .secure(true) // http가 아니면 쿠키 전송 안함
//                    .build();
//
//            return ResponseEntity.ok()
//                    .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
//                    .header(HttpHeaders.AUTHORIZATION,
//                            JwtProperties.BEARER_PREFIX + tokenDTO.getAccessToken())
//                    .build();
//        } else {
//            HttpCookie httpCookie = ResponseCookie.from("refresh-token", "")
//                    .maxAge(0)
//                    .path("/") // 모든 경로에 cookie를 사용하게 함
//                    .build();
//
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                    .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
//                    .build();
//        }
//    }

    /**
     * Cookie에 담긴 RT 삭제
     *
     * @param accessToken
     * @return
     */
//    @PostMapping("/logout")
//    public ResponseEntity<?> logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION) String accessToken) {
//        userService.logout(accessToken);
//
//        HttpCookie httpCookie = ResponseCookie.from("refresh-token", "")
//                .maxAge(0)
//                .path("/") // 모든 경로에 cookie를 사용하게 함
//                .build();
//
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .header(HttpHeaders.SET_COOKIE, httpCookie.toString())
//                .build();
//    }
}
