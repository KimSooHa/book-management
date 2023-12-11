package com.test.book.dto;

import lombok.Data;

@Data
public class BookResponseDto {

    private final String message;
    private final Long bookId;

}
