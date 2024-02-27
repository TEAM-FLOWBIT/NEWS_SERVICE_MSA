package com.example.boardservice.domain.board.repository;


import com.example.boardservice.domain.board.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImage,Long> {

    List<BoardImage> deleteAllByBoard_Id(Long boardId);

    List<BoardImage> findAllByBoard_Id(Long boardId);
}
