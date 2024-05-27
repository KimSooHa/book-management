package com.test.book.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;

@RedisHash("user")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 생성자 함수를 protected로 설정
@ToString(of = {"id", "name", "pwd", "email", "regDate"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull
    @Column(length = 18)
    private String name;

    @NotNull
    @Column(length = 10)
    private String loginId;

    @NotNull
    @Column(length = 16)
    private String pwd;

    @NotNull
    @Column(length = 320)
    private String email;

    @NotNull
    private LocalDateTime regDate;

    public User(String name, String loginId, String pwd, String email) {
        this.name = name;
        this.loginId = loginId;
        this.pwd = pwd;
        this.email = email;
        this.regDate = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user", cascade = REMOVE)
    private List<Loan> loans = new ArrayList<>();
}
