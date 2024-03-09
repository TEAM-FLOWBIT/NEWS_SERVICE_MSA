package com.example.boardservice.domain.news.entity;

import com.example.boardservice.domain.board.exception.error.MemberIdNullOrEmptyException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewsViewCount {
    @Column(name = "news_view_count")
    private Long newsViewCount;

    public NewsViewCount(Long newsViewCount) {
        this.newsViewCount = newsViewCount;
    }
    public void plusNewsViewCount(Long newsViewCount) {
        this.newsViewCount++;
    }
}
