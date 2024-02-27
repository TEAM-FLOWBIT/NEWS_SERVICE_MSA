package com.example.boardservice.domain.board.service;

import com.example.boardservice.domain.board.dto.BoardSearchCondition;
import com.example.boardservice.domain.board.dto.request.CreateBoardRequestDto;
import com.example.boardservice.domain.board.dto.request.UpdateBoardRequestDto;
import com.example.boardservice.domain.board.dto.response.CreateBoardResponseDto;
import com.example.boardservice.domain.board.dto.response.ReadBoardListResponseDto;
import com.example.boardservice.domain.board.dto.response.UpdateBoardResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    CreateBoardResponseDto createBoard(CreateBoardRequestDto createBoardResquestDto);

    Page<ReadBoardListResponseDto> readBoardList(Pageable pageable, BoardSearchCondition boardSearchCondition);

    void deleteBoard(Long boardId);

    UpdateBoardResponseDto updateBoard(UpdateBoardRequestDto updateBoardRequestDto,Long boardId);
}
