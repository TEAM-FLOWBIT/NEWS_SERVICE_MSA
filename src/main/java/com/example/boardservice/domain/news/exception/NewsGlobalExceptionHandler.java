package com.example.boardservice.domain.news.exception;

import com.example.boardservice.domain.board.exception.dto.CommonResponse;
import com.example.boardservice.domain.board.exception.dto.ErrorResponse;
import com.example.boardservice.domain.news.exception.error.NotFoundNewsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class NewsGlobalExceptionHandler {


    @ExceptionHandler(NotFoundNewsException.class)
    public ResponseEntity<Object> handleNotFoundNewsException(NotFoundNewsException ex) {
        log.error("handleNotFoundNewsException :: ");

        NewsErrorCode errorCode = NewsErrorCode.NOTFOUND_NEWS_EXCEPTION;


        ErrorResponse error = ErrorResponse.builder()
                .status(errorCode.getStatus().value())
                .message(errorCode.getMessage())
                .code(errorCode.getCode())
                .build();

        CommonResponse response = CommonResponse.builder()
                .success(false)
                .error(error)
                .build();

        return new ResponseEntity<>(response, errorCode.getStatus());
    }
}