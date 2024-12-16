package com.test.book.service;

import com.test.book.domain.Book;
import com.test.book.dto.BookRequestDto;
import com.test.book.dto.LoanHistoryDto;
import com.test.book.exception.FindBookException;
import com.test.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final LoanService loanService;

    /**
     * 도서 등록
     */
    @Transactional
    public Long save(BookRequestDto bookRequestDto) {
        Book book = new Book(bookRequestDto.getTitle(), bookRequestDto.getAuthor(), bookRequestDto.getIsbn(), LocalDate.parse(bookRequestDto.getPublicDate()));

        validateDuplicateBook(book.getIsbn());    // 중복 회원 검증
        bookRepository.save(book);
        return book.getId();
    }

    // 중복 도서 체크
    private void validateDuplicateBook(String isbn) {
        Optional<Book> findBook = bookRepository.findByIsbn(isbn);
        findBook.ifPresent((b) -> {
            throw new IllegalStateException("이미 존재하는 도서입니다.");
        });
    }

    @Transactional
    public void update(Long bookId, Map<String, String> requestBody) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new FindBookException("해당하는 도서를 찾을 수 없습니다."));
        if(requestBody.containsKey("title"))
            book.setTitle(requestBody.get("title"));

        if(requestBody.containsKey("author"))
            book.setAuthor(requestBody.get("author"));

        if(requestBody.containsKey("isbn")) {
            if(!Pattern.matches("^[\\d]{13}+$", requestBody.get("isbn")))
                throw new IllegalArgumentException("isbn 형식이 올바르지 않습니다.");
            try {
                validateDuplicateBook(requestBody.get("isbn"));
                book.setIsbn(requestBody.get("isbn"));
            } catch (IllegalStateException e) {
                throw new IllegalStateException("기존에 동일한 isbn이 존재합니다.");
            }
        }
        if(requestBody.containsKey("publicDate")) {
            if(!Pattern.matches("^[\\d]{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$", requestBody.get("publicDate")))
                throw new IllegalArgumentException("날짜는 하이픈(-)으로 구분지어줘야 합니다.");
            book.setPublicDate(LocalDate.parse(requestBody.get("publicDate")));
        }
    }

    public List<LoanHistoryDto> getLoanHistory(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new FindBookException("해당하는 도서를 찾을 수 없습니다."));
        return loanService.getHistoryList(book.getLoans());
    }

    @Transactional
    public void borrowBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new FindBookException("해당하는 도서를 찾을 수 없습니다."));
        // 대출 처리
        loanService.borrowBook(book, userId);
    }

    @Transactional
    public void returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new FindBookException("해당하는 도서를 찾을 수 없습니다."));
        // 반납 처리
        loanService.returnBook(book);
    }
}
