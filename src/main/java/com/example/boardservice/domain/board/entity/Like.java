package com.example.boardservice.domain.board.entity;

import com.example.boardservice.domain.board.exception.error.MemberIdNullOrEmptyException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like implements Serializable {

    @Column(name = "like_count")
    private Long likeCount;

    public Like(Long likeCount) {
        validateNull(likeCount);
        this.likeCount = likeCount;
    }
    public void plusLikeCount(Long likeCount) {
        validateNull(likeCount);
        this.likeCount++;
    }
    public void minusLikeCount(Long likeCount) {
        validateNull(likeCount);
        this.likeCount--;
    }
    private void validateNull(Long value) {
        if (Objects.isNull(value)) {
            throw new MemberIdNullOrEmptyException();
        }
    }
}
