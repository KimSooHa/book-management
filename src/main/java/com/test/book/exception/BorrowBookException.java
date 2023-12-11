package com.test.book.exception;

public class BorrowBookException extends IllegalStateException {


    public BorrowBookException(String message) {
        super(message);
    }

    public BorrowBookException(String message, Throwable cause) {
        super(message, cause);
    }

    public BorrowBookException(Throwable cause) {
        super(cause);
    }
}
