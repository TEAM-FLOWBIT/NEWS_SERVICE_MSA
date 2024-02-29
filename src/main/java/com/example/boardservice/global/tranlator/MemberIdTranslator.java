package com.example.boardservice.global.tranlator;

import com.example.boardservice.domain.board.entity.MemberId;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class MemberIdTranslator {

    public static MemberId getMemberId(Long memberId) {

        MemberId embededMemberId = new MemberId(memberId);

        return embededMemberId;
    }
}
