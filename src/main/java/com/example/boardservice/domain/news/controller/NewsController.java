package com.example.boardservice.domain.news.controller;

import com.example.boardservice.domain.news.dto.NewsSearchCondition;
import com.example.boardservice.domain.news.dto.response.NewsResponseDto;
import com.example.boardservice.domain.news.service.NewsService;
import com.example.boardservice.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<CommonResDto<?>> readNewsList(NewsSearchCondition newsSearchCondition,
                                                        Pageable pageable){

        Page<NewsResponseDto> result =newsService.readNewsList(newsSearchCondition, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"뉴스 데이터 읽기 성공",result));
    }

    @GetMapping("/view")
    public ResponseEntity<CommonResDto<?>> updateNewsViewCount(@RequestParam(value="link") String link){
        Long result = newsService.updateNewsViewCount(link);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"뉴스 조회수 업데이트",result));
    }

}