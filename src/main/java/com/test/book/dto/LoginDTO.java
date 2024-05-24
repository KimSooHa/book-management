package com.test.book.dto;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class LoginDTO {
    private String loginId;
    private String pwd;
}
