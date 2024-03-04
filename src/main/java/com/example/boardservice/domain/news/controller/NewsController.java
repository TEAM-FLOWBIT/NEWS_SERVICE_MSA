package com.example.boardservice.domain.news.controller;

import com.example.boardservice.domain.news.dto.NewsResponseDto;
import com.example.boardservice.domain.news.service.NewsService;
import com.example.boardservice.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController {

    private final Environment env;
    private final NewsService newsService;


    @GetMapping("/health_check")
    public String status(){

        return String.format("It's Working in Order Service on PORT %s"
                , env.getProperty("local.server.port"));

    }

    @GetMapping("")
    public ResponseEntity<CommonResDto<?>> getNews(@RequestParam String searchWord,
                                                   @RequestParam Long display) throws Exception {

        List<NewsResponseDto> result = newsService.getNewsData(searchWord, display);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"뉴스 데이터 생성",result));
    }

}