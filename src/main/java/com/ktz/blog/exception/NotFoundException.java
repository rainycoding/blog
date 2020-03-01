package com.ktz.blog.exception;

/**
 * Created by 曾泉明 on 2020/2/26 3:52
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
