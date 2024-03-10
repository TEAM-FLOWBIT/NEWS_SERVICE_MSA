package com.example.boardservice.domain.news.dto.response;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
public class NaverNewsResponseDto {
    private String title;
    private String preview_link;
    private String pubDate;
    private String description;
    private String originalLink;
    private String img;
    private String tag;
}