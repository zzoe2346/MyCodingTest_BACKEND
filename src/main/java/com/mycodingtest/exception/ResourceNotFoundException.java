package com.mycodingtest.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super("Not found your resource");
    }
}
