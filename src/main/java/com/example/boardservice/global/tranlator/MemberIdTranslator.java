package com.example.boardservice.global.tranlator;

import com.example.boardservice.domain.board.entity.MemberId;

public class MemberIdTranslator {

    public static MemberId getMemberId(Long memberId) {

        return new MemberId(memberId);
    }
}
