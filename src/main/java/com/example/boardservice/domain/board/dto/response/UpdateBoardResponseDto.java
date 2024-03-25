package com.example.boardservice.domain.board.dto.response;

import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.BoardTags;
import com.example.boardservice.global.client.dto.MemberInfoResponseDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class UpdateBoardResponseDto {
    private Long boardId;
    private String title;
    private String content;
    private String memberId;
    private String memberNickname;
    private List<String> boardTags;
    private List<String> imagePaths;


    @Builder
    public UpdateBoardResponseDto(Board board, MemberInfoResponseDto memberInfoResponseDto, List<String> imagePaths, List<BoardTags> boardTags) {
        this.boardId = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.memberId=memberInfoResponseDto.getEmail();
        this.memberNickname=memberInfoResponseDto.getNickname();
        this.boardTags=boardTags.stream().map(bt->bt.getTag().getWord()).collect(Collectors.toList());
        this.imagePaths=imagePaths;
    }
}
