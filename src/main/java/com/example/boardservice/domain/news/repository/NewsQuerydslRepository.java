package com.example.boardservice.domain.news.repository;
import com.example.boardservice.domain.news.dto.NewsSearchCondition;
import com.example.boardservice.domain.news.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsQuerydslRepository {
    Page<News> readBoardList(NewsSearchCondition newsSearchCondition,Pageable pageable);

}
