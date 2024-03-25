package com.example.boardservice.domain.board.dto.response;

import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.BoardTags;
import com.example.boardservice.global.client.dto.MemberInfoResponseDto;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class CreateBoardResponseDto {
    private Long boardId;
    private String title;
    private String content;
    private String memberId;
    private String memberNickname;
    private Long boardLikeCount;
    private Long boardCommentCount;
    private String boardCategory;
    private List<String> imagePaths;
    private List<String> tags;


    @Builder
    public CreateBoardResponseDto(Board board, MemberInfoResponseDto memberInfoResponseDto, List<String> imagePaths, List<BoardTags> boardTags) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.memberId=memberInfoResponseDto.getEmail();
        this.memberNickname=memberInfoResponseDto.getNickname();
        this.boardLikeCount=board.getLike().getLikeCount();
        this.boardCommentCount=board.getBoardCommentCount().getBoardCommentCount();
        this.boardCategory=board.getBoardCategory().getLabel();
        this.imagePaths=imagePaths;
        this.tags=boardTags.stream().map(boardTag->boardTag.getTag().getWord()).collect(Collectors.toList());
    }
}
