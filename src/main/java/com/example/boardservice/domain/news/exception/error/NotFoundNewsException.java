package com.example.boardservice.domain.news.exception.error;

public class NotFoundNewsException extends RuntimeException{
    public NotFoundNewsException() {
        super();
    }
    public NotFoundNewsException(String message, Throwable cause) {
        super(message, cause);
    }
    public NotFoundNewsException(String message) {
        super(message);
    }
    public NotFoundNewsException(Throwable cause) {
        super(cause);
    }

}
