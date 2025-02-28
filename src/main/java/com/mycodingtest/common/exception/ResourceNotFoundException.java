package com.mycodingtest.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Not found your resource");
    }
}
