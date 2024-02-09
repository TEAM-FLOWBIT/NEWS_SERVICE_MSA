package com.example.newsservice.domain.news.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
public class NewsResponseDto {
    private String title;
    private String link;
    private String pubDate;
    private String description;
    private String originalling;
    private String img;
}