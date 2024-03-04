package com.example.boardservice.domain.board.entity;

import com.example.boardservice.domain.board.exception.error.MemberIdNullOrEmptyException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardCommentCount {

    @Column(name = "board_comment_count")
    private Long boardCommentCount;

    public BoardCommentCount(Long boardCommentCount) {
        validateNull(boardCommentCount);
        this.boardCommentCount = boardCommentCount;
    }
    public void plusBoardCommentCount(Long boardCommentCount) {
        validateNull(boardCommentCount);
        this.boardCommentCount++;
    }
    public void minusBoardCommentCount(Long boardCommentCount) {
        validateNull(boardCommentCount);
        this.boardCommentCount--;
    }
    private void validateNull(Long value) {
        if (Objects.isNull(value)) {
            throw new MemberIdNullOrEmptyException();
        }
    }
}
