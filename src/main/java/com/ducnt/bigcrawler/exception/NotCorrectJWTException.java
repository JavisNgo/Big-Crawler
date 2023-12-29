package com.ducnt.bigcrawler.exception;

public class NotCorrectJWTException extends RuntimeException{
    public NotCorrectJWTException(String message) {
        super(message);
    }
}
