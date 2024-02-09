package com.example.newsservice.domain.news.controller;

import com.example.newsservice.domain.news.dto.NewsResponseDto;
import com.example.newsservice.domain.news.service.NewsService;
import com.example.newsservice.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
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