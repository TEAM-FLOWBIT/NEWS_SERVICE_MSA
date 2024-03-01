package com.example.boardservice.domain.board.repository;

import com.example.boardservice.domain.board.dto.BoardSearchCondition;
import com.example.boardservice.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardQuerydslRepository {

    Page<Board> readBoardList(Pageable pageable, BoardSearchCondition boardSearchCondition);

    Page<Board> readTopWeeklyLikedBoards(Pageable pageable);
}
