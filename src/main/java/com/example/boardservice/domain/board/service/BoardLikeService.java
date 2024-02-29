package com.example.boardservice.domain.board.service;

public interface BoardLikeService {

    Long boardLike(Long boardId);
    Long boardUnlike(Long boardId);
}
