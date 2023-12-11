package com.test.book.exception;

public class FindBookException extends IllegalArgumentException {


    public FindBookException(String message) {
        super(message);
    }

    public FindBookException(String message, Throwable cause) {
        super(message, cause);
    }

    public FindBookException(Throwable cause) {
        super(cause);
    }
}
