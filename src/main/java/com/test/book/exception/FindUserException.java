package com.test.book.exception;

public class FindUserException extends IllegalArgumentException {


    public FindUserException(String message) {
        super(message);
    }

    public FindUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public FindUserException(Throwable cause) {
        super(cause);
    }
}
