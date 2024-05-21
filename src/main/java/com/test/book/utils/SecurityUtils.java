package com.test.book.utils;

import com.test.book.component.CustomUserDetails;
import com.test.book.exception.FindUserException;
import com.test.book.exception.ValidateUserException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityUtils {

    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private static Authentication getUserAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // set 할 때 idx를 넣어서 하는 방안
        return authentication;
    }

    public static String getEmail() { // 시큐리티를 지나서 authentication에 등록된 유저 아이디를 가져오는 메서드
        CustomUserDetails user = (CustomUserDetails) getUserAuthentication().getPrincipal();
        if (user != null) {
            return user.getEmail();
        } else {
            // 유저 정보가 없는 OAuth2 클라이언트, Anonymous 및 잘못된 인가 정보일 경우 유저네임이 없는 것으로 가정함
            return null;
        }
    }

    public static Long getUserIdx() { // 시큐리티를 지나서 authentication에 등록된 유저 아이디를 가져오는 메서드
        CustomUserDetails user = (CustomUserDetails) getUserAuthentication().getPrincipal();
        if (user != null) {
            return user.getUserIdx();
        } else {
            // 유저 정보가 없는 OAuth2 클라이언트, Anonymous 및 잘못된 인가 정보일 경우 유저네임이 없는 것으로 가정함
            return null;
        }
    }

    public static String encodeStr(String str) {
        return encoder.encode(str);
    }

    // 비밀번호 변경 시 이전 비밀번호가 맞는지 체크
    public static void prevPwdMatch(String currentPwd, String changePwd) throws FindUserException {
        if (!(encoder.matches(currentPwd, changePwd))) {
            throw new ValidateUserException("UserPwd is not validate !!");
        }
    }

    // 비밀번호 변경 시 이전 비밀번호와 바꿀 비밀번호가 같은지 체크
    public static void pwdMatch(String currentPwd, String changePwd) {
        if (encoder.matches(currentPwd, changePwd)) {
            throw new ValidateUserException("UserPwd is duplicate !!");
        }
    }

}
