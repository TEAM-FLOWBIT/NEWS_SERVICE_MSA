package com.example.boardservice.domain.board.dto.response;

import com.example.boardservice.domain.board.entity.BoardComment;
import com.example.boardservice.global.client.dto.MemberInfoResponseDto;
import lombok.*;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class CreatedBoardCommentResponseDto {

    private Long boardId;
    private String content;
    private String memberId;
    private String name;
    private String profile;

    @Builder
    public CreatedBoardCommentResponseDto(BoardComment boardComment, MemberInfoResponseDto memberInfoResponseDto) {
        this.boardId = boardComment.getBoard().getId();
        this.content = boardComment.getContent();
        this.memberId = memberInfoResponseDto.getEmail();
        this.name = memberInfoResponseDto.getNickname();
        this.profile = memberInfoResponseDto.getProfile();
    }
}
