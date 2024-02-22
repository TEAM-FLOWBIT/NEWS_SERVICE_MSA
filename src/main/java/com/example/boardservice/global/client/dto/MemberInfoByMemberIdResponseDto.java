package com.example.boardservice.global.client.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoByMemberIdResponseDto {
    private Long id;
    private String nickname;
    private String profile;
    private String email;

    @Builder
    public MemberInfoByMemberIdResponseDto(Long id, String nickname, String profile, String email) {
        this.id = id;
        this.nickname=nickname;
        this.profile=profile;
        this.email=email;
    }
}
