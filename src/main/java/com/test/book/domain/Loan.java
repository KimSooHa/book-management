package com.test.book.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "borrowedDate", "returnedDate"})
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long id;

    @NotNull
    private LocalDateTime borrowedDate;

    private LocalDateTime returnedDate;


    public Loan() {
        this.borrowedDate = LocalDateTime.now();
    }

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    //== 연관관계 메서드==//
    // 양방향으로 연관관계의 값을 세팅
    public void setUser(User user) {
        this.user = user;
        user.getLoans().add(this);
    }

    public void setBook(Book book) {
        this.book = book;
        book.getLoans().add(this);
    }

    //==생성 메서드==//
    public static Loan createLoan(Loan loan, User user, Book book) {
        loan.setUser(user);
        loan.setBook(book);
        return loan;
    }
}
