package com.example.boardservice.domain.board.exception.error;

public class DuplicateLikeException extends RuntimeException{
    public DuplicateLikeException() {
        super();
    }
    public DuplicateLikeException(String message, Throwable cause) {
        super(message, cause);
    }
    public DuplicateLikeException(String message) {
        super(message);
    }
    public DuplicateLikeException(Throwable cause) {
        super(cause);
    }

}
