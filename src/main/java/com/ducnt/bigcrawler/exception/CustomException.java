package com.ducnt.bigcrawler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice

public class CustomException {
    @ExceptionHandler(NotCorrectJWTException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handlerJWTException(NotCorrectJWTException ex) {
        return new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
