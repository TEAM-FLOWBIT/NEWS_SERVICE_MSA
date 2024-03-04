package com.example.boardservice.domain.board.service;

import com.example.boardservice.domain.board.dto.request.CreateBoardCommentRequestDto;
import com.example.boardservice.domain.board.dto.response.CreatedBoardCommentResponseDto;

public interface BoardCommentService {

    CreatedBoardCommentResponseDto createBoardComment(CreateBoardCommentRequestDto createBoardCommentRequestDto);

    void deleteBoardComment(Long commentId);
}
