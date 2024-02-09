package com.example.newsservice.domain.news.service;

import com.example.newsservice.domain.news.dto.NewsResponseDto;

import java.util.List;

public interface NewsService {

    List<NewsResponseDto> getNewsData(String searchWord,Long display) throws Exception;
}
