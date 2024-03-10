package com.example.boardservice.domain.news.repository;

import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.repository.BoardQuerydslRepository;
import com.example.boardservice.domain.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsRepository extends JpaRepository<News,Long>, NewsQuerydslRepository {

    Optional<News> findByLink(String link);

}
