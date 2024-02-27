package com.example.boardservice.domain.board.dto.response;

import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.global.client.dto.MemberInfoResponseDto;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class CreateBoardResponseDto {
    private Long boardId;
    private String title;
    private String content;
    private String memberId;
    private String memberNickname;
    private List<String> imagePaths;


    @Builder
    public CreateBoardResponseDto(Board board, MemberInfoResponseDto memberInfoResponseDto, List<String> imagePaths) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.memberId=memberInfoResponseDto.getEmail();
        this.memberNickname=memberInfoResponseDto.getNickname();
        this.imagePaths=imagePaths;
    }
}
