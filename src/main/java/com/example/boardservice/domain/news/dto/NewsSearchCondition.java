package com.example.boardservice.domain.news.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NewsSearchCondition {
    private String tag;
    private String searchword;
}

