package com.example.boardservice.domain.news.entity;


import com.example.boardservice.domain.board.entity.Like;
import com.example.boardservice.global.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Arrays;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "NEWS", uniqueConstraints = { @UniqueConstraint(columnNames = { "link" }) })
@Where(clause = "deleted_at IS NULL")
public class News extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NEWS_ID")
    private Long id;
    @Column(nullable = false, length = 1000)
    private String title;
    @Column(length = 200,nullable = false)
    private String link;
    @Column(nullable = false, length = 1000)
    private String pubDate;
    @Column(nullable = false, length = 1000)
    private String description;
    @Column(nullable = false, length = 1000)
    private String originalLink;
    @Column(nullable = false, length = 1000)
    private String img;
    @Column(nullable = false, length = 1000)
    private String tag;
    @Embedded
    private NewsViewCount newsViewCount;




    public void updateNews(News news) {
        this.id = news.getId();
        this.title = news.getTitle();
        this.link = news.getLink();
        this.pubDate = news.getPubDate();
        this.description = news.getDescription();
        this.originalLink = news.getOriginalLink();
        this.img = news.getImg();
        this.tag = news.getTag();
        this.newsViewCount=news.getNewsViewCount();
    }

    public void updateTag(String tag){
        this.tag=tag;
    }

    public boolean isDuplicateTag(String newTag) {
        String[] tags = this.tag.split(",");
        return Arrays.stream(tags)
                .anyMatch(tag -> tag.equals(newTag));
    }

    /**
     * newsViewCount 동기화
     */
    public void plusNewsViewCount(Long newsViewCount){
        this.newsViewCount.plusNewsViewCount(newsViewCount);
    }
}
