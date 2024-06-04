package com.example.boardservice.domain.news.service;

import com.example.boardservice.domain.news.dto.response.NaverNewsResponseDto;
import com.example.boardservice.domain.news.dto.NewsSearchCondition;
import com.example.boardservice.domain.news.dto.response.NewsResponseDto;
import com.example.boardservice.domain.news.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NewsService {

    List<NaverNewsResponseDto> readNaverNewsData(String searchWord, Long display) throws Exception;

    void saveNewsApiData(List<NaverNewsResponseDto> newsData) throws Exception;

    Page<News> readNewsList(NewsSearchCondition newsSearchCondition, Pageable pageable);

    Long updateNewsViewCount(String link);
}
