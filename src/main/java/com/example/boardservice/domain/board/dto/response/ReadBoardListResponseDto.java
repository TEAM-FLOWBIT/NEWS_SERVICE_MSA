package com.example.boardservice.domain.board.dto.response;


import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.BoardImage;
import com.example.boardservice.global.client.dto.MemberInfoByMemberIdResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class ReadBoardListResponseDto {
    private Long memberId;
    private String memberEmail;
    private String nickname;
    private String profile;
    private Long boardId;
    private String title;
    private String content;
    private String createTime;
    private List<String> imagePath;
    private List<BoardListWithComment> comments=new ArrayList<>();

    @Builder
    public ReadBoardListResponseDto(Board board, MemberInfoByMemberIdResponseDto memberInfoByMemberIdResponseDto) {
        this.memberEmail=memberInfoByMemberIdResponseDto.getEmail();
        this.memberId =memberInfoByMemberIdResponseDto.getId();
        this.nickname = memberInfoByMemberIdResponseDto.getNickname();
        this.profile = memberInfoByMemberIdResponseDto.getProfile();
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createTime = String.valueOf(board.getCreatedAt());
        this.imagePath = board.getBoardImages().stream().map(BoardImage::getImage).collect(Collectors.toList());
        this.comments = board.getBoardComments().stream().map(boardComment -> new BoardListWithComment(boardComment,memberInfoByMemberIdResponseDto)).collect(Collectors.toList());
    }
}
