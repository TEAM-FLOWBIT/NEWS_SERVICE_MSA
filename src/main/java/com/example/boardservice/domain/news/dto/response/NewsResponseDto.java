package com.example.boardservice.domain.news.dto.response;


import com.example.boardservice.domain.news.entity.News;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NewsResponseDto {
    private String title;
    private String preview_link;
    private String pubDate;
    private String description;
    private String originalLink;
    private String img;
    private List<String> tag;
    private Long newsViewCount;

    @Builder
    public NewsResponseDto(News news) {
        this.title = news.getTitle();
        this.preview_link = news.getLink();
        this.pubDate = news.getPubDate();
        this.description = news.getDescription();
        this.originalLink = news.getOriginalLink();
        this.img = news.getImg();
        this.tag = List.of(news.getTag().split(","));
        this.newsViewCount=news.getNewsViewCount().getNewsViewCount();
    }
}
