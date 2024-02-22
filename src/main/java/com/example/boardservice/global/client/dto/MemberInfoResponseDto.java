package com.example.boardservice.global.client.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoResponseDto {
    private Long id;
    private String nickname;
    private String profile;
    private String email;

    @Builder
    public MemberInfoResponseDto(Long id,String nickname,String profile,String email) {
        this.id = id;
        this.nickname=nickname;
        this.profile=profile;
        this.email=email;
    }
}
