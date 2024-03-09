package com.example.boardservice.domain.board.dto.request;

import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.BoardComment;
import com.example.boardservice.domain.board.entity.MemberId;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateBoardCommentRequestDto {
    @NotEmpty(message = "게시글을 입력해주세요.")
    private Long boardId;
    @NotEmpty(message ="내용을 입력해주세요.")
    private String content;


    public BoardComment toEntity(MemberId memberId, Board board, String content) {
        return BoardComment.builder()
                .board(board)
                .memberId(memberId)
                .content(content)
                .build();
    }
}
