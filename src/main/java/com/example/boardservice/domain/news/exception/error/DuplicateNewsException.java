package com.example.boardservice.domain.news.exception.error;

public class DuplicateNewsException extends RuntimeException{
    public DuplicateNewsException() {
        super();
    }
    public DuplicateNewsException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateNewsException(String message) {
        super(message);
    }
    public DuplicateNewsException(Throwable cause) {
        super(cause);
    }

}
