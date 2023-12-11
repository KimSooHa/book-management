package com.test.book.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoanHistoryDto {

    private Long loanId;
    private Long userId;
    private String borrowedDate;
    private String returnedDate;
}
