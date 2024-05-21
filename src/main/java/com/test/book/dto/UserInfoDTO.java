package com.test.book.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {
    private int userIdx;
    private String email;
    private String id;
    private String nickname;
    private String pwd;
}
