package com.example.boardservice.domain.board.dto.response;


import com.example.boardservice.domain.board.entity.BoardComment;
import com.example.boardservice.global.client.dto.MemberInfoByMemberIdResponseDto;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class BoardListWithComment {
    private String memberEmail;
    private String profile;
    private String createTime;
    private String content;
    private Long commentId;
    private Long memberId;
    private String name;

    @Builder
    public BoardListWithComment(BoardComment boardComment, MemberInfoByMemberIdResponseDto memberInfoByMemberIdResponseDto) {
        this.memberEmail=memberInfoByMemberIdResponseDto.getEmail();
        this.memberId = memberInfoByMemberIdResponseDto.getId();
        this.profile =memberInfoByMemberIdResponseDto.getProfile();
        this.createTime = String.valueOf(boardComment.getCreatedAt());
        this.content = boardComment.getContent();
        this.commentId=boardComment.getId();
        this.name=memberInfoByMemberIdResponseDto.getNickname();
    }
}