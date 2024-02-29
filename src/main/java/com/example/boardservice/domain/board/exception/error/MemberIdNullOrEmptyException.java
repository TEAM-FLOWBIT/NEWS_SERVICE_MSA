package com.example.boardservice.domain.board.exception.error;


public class MemberIdNullOrEmptyException extends RuntimeException {
    public MemberIdNullOrEmptyException() {
        super();
    }
    public MemberIdNullOrEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
    public MemberIdNullOrEmptyException(String message) {
        super(message);
    }
    public MemberIdNullOrEmptyException(Throwable cause) {
        super(cause);
    }
}