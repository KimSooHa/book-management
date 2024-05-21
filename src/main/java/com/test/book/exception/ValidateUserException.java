package com.test.book.exception;

public class ValidateUserException extends IllegalArgumentException {


    public ValidateUserException(String message) {
        super(message);
    }

    public ValidateUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateUserException(Throwable cause) {
        super(cause);
    }
}
