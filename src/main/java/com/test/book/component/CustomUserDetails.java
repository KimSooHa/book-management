package com.test.book.component;

import com.test.book.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 사용자 인증정보 제공 객체
 */
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> collect = new ArrayList<>();
//        collect.add(new SimpleGrantedAuthority("테스트"));
//        return collect;
        // 특별한 권한 시스템을 사용하지 않을 경우
        // return Collections.EMPTY_LIST;
        // 를 사용하면 된다.
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getPassword() {
        return user.getPwd();
    }

    public String getId() {
        return user.getLoginId();
    }

    public User getUser() {
        return user;
    }

    public String getEmail() {
        return user.getEmail();
    }

    public Long getUserIdx() {
        return user.getId();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    // 계정 만료여부 제공
    // 특별히 사용을 안할 시 항상 true를 반환하도록 처리
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 비활성화 여부 제공
    // 특별히 사용 안할 시 항상 true를 반환하도록 처리
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 인증 정보를 항상 저장할지에 대한 여부
    // true 처리할시 모든 인증정보를 만료시키지 않기에 주의해야 한다.
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    // 계정의 활성화 여부
    // 딱히 사용안할시 항상 true를 반환하도록 처리
    @Override
    public boolean isEnabled() {
        return true;
    }
}
