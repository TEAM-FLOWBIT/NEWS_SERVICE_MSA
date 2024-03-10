package com.example.boardservice.domain.news.exception.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private int status;
}