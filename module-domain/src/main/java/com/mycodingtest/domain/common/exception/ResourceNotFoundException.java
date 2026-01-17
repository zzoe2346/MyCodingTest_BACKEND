package com.mycodingtest.domain.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Not found your resource");
    }
}
