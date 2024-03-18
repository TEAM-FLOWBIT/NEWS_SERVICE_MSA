package com.example.boardservice.domain.board.service;

import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.BoardTags;

import java.util.List;

public interface BoardTagService {

    List<BoardTags> createBoardTag(Board board, String tags);

    List<BoardTags>updateBoardTag(Board board,String tags);
}
