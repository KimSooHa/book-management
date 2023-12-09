package com.test.book.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "title", "author", "isbn", "publicDate", "borrowed", "borrowerId", "borrowedDate"})
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long id;

    @NotNull
    @Column(length = 20)
    private String title;

    @Column(length = 200)
    private String author;

    @Column(length = 13)
    @NotNull
    private String isbn; // 도서번호

    @NotNull
    private LocalDate publicDate;

    @NotNull
    private boolean borrowed;

    private Long borrowerId;

    private LocalDate borrowedDate;


    public Loan(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.borrowed = false;
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
