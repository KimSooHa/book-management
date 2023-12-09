package com.test.book.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.REMOVE;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "title", "author", "isbn", "publicDate", "borrowed", "borrowerId", "borrowedDate"})
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
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


    public Book(String title, String author, String isbn, boolean borrowed) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.borrowed = false;
    }

    @OneToMany(mappedBy = "book", cascade = REMOVE)
    private List<Loan> loans = new ArrayList<>();

}
