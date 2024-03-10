package com.example.boardservice.global.tranlator;

import com.example.boardservice.domain.board.entity.MemberId;
import com.example.boardservice.domain.news.entity.NewsViewCount;

public class Translator {

    public static MemberId getMemberId(Long memberId) {

        return new MemberId(memberId);
    }

    public static NewsViewCount getNewsViewCount(Long newsViewCount) {

        return new NewsViewCount(newsViewCount);
    }
}

