package com.example.boardservice.domain.board.repository;

import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag,Long>{
    boolean existsByWord(String word);

    List<Tag> findByWordIn(List<String> tags);
}
