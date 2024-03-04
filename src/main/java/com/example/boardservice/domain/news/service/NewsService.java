package com.example.boardservice.domain.news.service;

import com.example.boardservice.domain.news.dto.NewsResponseDto;

import java.util.List;

public interface NewsService {

    List<NewsResponseDto> getNewsData(String searchWord,Long display) throws Exception;
}
