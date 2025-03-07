package com.mycodingtest.common.exception;

public class InvalidOwnershipException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Access is forbidden for this resource. Because not your resource.";

    public InvalidOwnershipException() {
        super(DEFAULT_MESSAGE);
    }
}
