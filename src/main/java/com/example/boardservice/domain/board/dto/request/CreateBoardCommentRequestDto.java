package com.example.boardservice.domain.board.dto.request;

import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.BoardComment;
import com.example.boardservice.domain.board.entity.MemberId;
import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateBoardCommentRequestDto {
    @NotBlank
    private Long boardId;
    @NotBlank
    private String content;


    public BoardComment toEntity(MemberId memberId, Board board, String content) {
        return BoardComment.builder()
                .board(board)
                .memberId(memberId)
                .content(content)
                .build();
    }
}
