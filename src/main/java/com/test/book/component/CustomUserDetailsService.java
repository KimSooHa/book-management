//package com.test.book.component;
//
//import com.test.book.domain.User;
//import com.test.book.repository.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
///**
// * UserDetails 를 토대로 사용자의 인증정보를 SecurityContextHolder 에 제공
// */
//@Slf4j
//@Service
//public class CustomUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    UserRepository userRepository;
//
//    /**
//     * 비밀번호를 사용안하는 이유?
//     * 보통 Database에 비밀번호를 암호화해서 저장하므로 Database 에서 해당 정보를 찾을 때
//     * ID값과 암호화된 비밀번호를 비교해가면서 찾는 것보다 ID 값으로 먼저 찾고 비밀번호를 복호화해서 비교하는게 더 빠르고 정확하다.
//     * -> 때문에 대부분 회원가입할 때 ID 중복을 확인하는 이유가 ID 값으로 찾았을 때 여러개의 계정정보가 검색되면 어떤 계정으로 인증을 해야할지 알수없기 때문.
//     * @param loginId
//     * @return
//     * @throws UsernameNotFoundException
//     */
//    @Override
//    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
//        log.info("loadUserByUsername in !!");
//        // username으로 Repository에서 User를 찾는 method
//        User user = userRepository.findByLoginId(loginId)
//                    .orElseThrow(() -> new UsernameNotFoundException("계정을 찾을 수 없습니다."));
//
//        return new CustomUserDetails(user); // 가져온 유저 정보를 UserDetails 에 넣어서 세팅해주기
//    }
//}
