package com.example.boardservice.domain.board.repository;


import com.example.boardservice.domain.board.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment,Long> {
}
