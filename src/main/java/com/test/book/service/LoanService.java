package com.test.book.service;

import com.test.book.domain.Book;
import com.test.book.domain.Loan;
import com.test.book.domain.User;
import com.test.book.dto.LoanHistoryDto;
import com.test.book.exception.BorrowBookException;
import com.test.book.exception.FindUserException;
import com.test.book.repository.LoanRepository;
import com.test.book.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public List<LoanHistoryDto> getHistoryList(List<Loan> loans) {
        if(loans.isEmpty())
            throw new IllegalStateException("해당 도서의 대출이력이 없습니다.");

        List<LoanHistoryDto> loanHistoryDtos = new ArrayList<>();
        loans.forEach(l -> {
            LoanHistoryDto dto = new LoanHistoryDto();
            dto.setLoanId(l.getId());
            dto.setUserId(l.getUser().getId());
            dto.setBorrowedDate(l.getBorrowedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            dto.setReturnedDate(l.getReturnedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            loanHistoryDtos.add(dto);
        });
        return loanHistoryDtos;
    }

    @Transactional
    public void borrowBook(Book book, Long userId) {
        Loan loan = new Loan();
        User user = userRepository.findById(userId).orElseThrow(() -> new FindUserException("해당하는 회원이 없습니다."));
        if(book.isBorrowed())
            throw new BorrowBookException("이미 대출상태의 도서입니다.");
        Book.borrowBook(book, userId);
        Loan.createLoan(loan, user, book);
        loanRepository.save(loan);
    }

    @Transactional
    public void returnBook(Book book) {
        Loan loan = book.getLoans().stream()
                .filter(l -> l.getReturnedDate() == null)
                .findFirst().orElseThrow(() -> new IllegalStateException("해당 도서의 대출상태를 찾을 수 없습니다."));
        loan.setReturnedDate(LocalDateTime.now());
        Book.returnBook(book);
//        loanRepository.save(loan);
    }
}
