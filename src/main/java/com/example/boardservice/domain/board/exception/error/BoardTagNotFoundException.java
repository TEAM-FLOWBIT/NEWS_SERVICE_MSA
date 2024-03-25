package com.example.boardservice.domain.board.exception.error;

public class BoardTagNotFoundException extends RuntimeException{
    public BoardTagNotFoundException() {
        super();
    }
    public BoardTagNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    public BoardTagNotFoundException(String message) {
        super(message);
    }
    public BoardTagNotFoundException(Throwable cause) {
        super(cause);
    }

}
