package com.example.boardservice.domain.board.entity;

import com.example.boardservice.domain.board.exception.error.MemberIdNullOrEmptyException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberId implements Serializable {

    @Column(name = "member_id")
    private Long id;

    public MemberId(Long memberId) {
        validateNullOrEmpty(memberId);
        this.id = memberId;
    }
    private void validateNullOrEmpty(Long value) {
        if (Objects.isNull(value)) {
            throw new MemberIdNullOrEmptyException();
        }
    }
}
