package com.example.boardservice.domain.board.repository;

import com.example.boardservice.domain.board.entity.BoardTags;
import com.example.boardservice.domain.board.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardTagRepository  extends JpaRepository<BoardTags,Long> {

    Optional<BoardTags> findByBoard_Id(Long boardId);
    List<BoardTags> findAllByBoard_Id(Long boardId);

    void deleteAllByBoard_Id(Long boardId);
}
