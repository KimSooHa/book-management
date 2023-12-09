package com.test.book.service;

import com.test.book.domain.User;
import com.test.book.dto.UserRequestDto;
import com.test.book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

}
