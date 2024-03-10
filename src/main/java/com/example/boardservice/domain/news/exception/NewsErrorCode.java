package com.example.boardservice.domain.news.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum NewsErrorCode {

    NOTFOUND_NEWS_EXCEPTION(HttpStatus.NOT_FOUND, "NEWS_001", "뉴스를 찾을 수 없습니다.");


    private final String code;
    private final String message;
    private final HttpStatus status;

    NewsErrorCode(final HttpStatus status, final String code, final String message){
        this.status = status;
        this.message = message;
        this.code = code;
    }
}