package com.test.book.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class BookRequestDto {

    @NotBlank(message = "도서명을 작성해주세요.")
    private String title;

    @NotBlank(message = "작가명을 작성해주세요.")
    private String author;

    @NotBlank(message = "isbn을 작성해주세요.")
    @Pattern(regexp = "^[\\d]{13}+$", message = "13자리 숫자로만 이뤄져야 합니다.")
    @Size(min = 13, max = 13, message = "국제표준도서번호는 13자리여야 합니다.")
    private String isbn;

    @NotBlank(message = "출판일을 작성해주세요.")
    @Pattern(regexp = "^[\\d]{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", message = "날짜는 하이픈(-)으로 구분지어줘야 합니다.")
    private String publicDate;
}
