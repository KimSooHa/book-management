package com.test.book.controller;

import com.test.book.dto.UserRequestDto;
import com.test.book.dto.UserResponseDto;
import com.test.book.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;


    /**
     * 회원가입
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

}
