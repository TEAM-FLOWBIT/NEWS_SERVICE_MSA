package com.example.boardservice.domain.board.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardCategory {
    BITCOIN("비트코인"),
    ETHEREUM("이더리움"),
    RIPPLE("리플");

    private final String label;
}