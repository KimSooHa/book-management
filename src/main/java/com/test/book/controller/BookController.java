package com.test.book.controller;

import com.test.book.dto.BookRequestDto;
import com.test.book.dto.BookResponseDto;
import com.test.book.dto.LoanHistoryDto;
import com.test.book.exception.BorrowBookException;
import com.test.book.exception.FindBookException;
import com.test.book.exception.FindUserException;
import com.test.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
@Slf4j
public class BookController {

    private final BookService bookService;


    /**
     * 도서 등록
     */
    @PostMapping
    public ResponseEntity create(@RequestBody @Valid BookRequestDto bookRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            log.info("errors={}", result);
            return ResponseEntity.badRequest().body("Invalid input: " + result.getAllErrors());
        }
        try {
            Long bookId = bookService.save(bookRequestDto);
            log.info("도서등록 성공!");
            return ResponseEntity.ok().body(new BookResponseDto("도서등록 성공!", bookId));
        } catch (FindBookException e) {
            // 도서 등록 실패 시 예외 처리
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 도서 수정
     */
    @PutMapping("/{bookId}")
    public ResponseEntity update(@PathVariable Long bookId, @RequestBody Map<String, String> requestBody) {
        try {
            bookService.update(bookId, requestBody);
            return ResponseEntity.ok().body("도서정보가 정상적으로 수정되었습니다!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 대출 이력 확인
     */
    @GetMapping("/{bookId}/loan-history")
    public ResponseEntity getLoanHistory(@PathVariable Long bookId) {
        try {
            List<LoanHistoryDto> loanHistory = bookService.getLoanHistory(bookId);
            return ResponseEntity.ok().body(loanHistory);
        } catch (FindBookException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 대출 처리
     */
    @PostMapping("/{bookId}/loan")
    public ResponseEntity borrowBook(@PathVariable Long bookId, @RequestParam Long userId) {
        try {
            bookService.borrowBook(bookId, userId);
            return ResponseEntity.ok().body("도서 대출이 정상적으로 처리되었습니다!");
        } catch (FindBookException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (FindUserException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (BorrowBookException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 반납 처리
     */
    @PostMapping("/{bookId}/return")
    public ResponseEntity returnBook(@PathVariable Long bookId) {
        try {
            bookService.returnBook(bookId);
            return ResponseEntity.ok().body("정상적으로 반납처리되었습니다!");
        } catch (FindBookException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
