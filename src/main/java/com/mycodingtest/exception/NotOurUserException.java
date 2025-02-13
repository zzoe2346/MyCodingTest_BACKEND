package com.mycodingtest.exception;

public class NotOurUserException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Not our user.";

    public NotOurUserException() {
        super(DEFAULT_MESSAGE);
    }
}
