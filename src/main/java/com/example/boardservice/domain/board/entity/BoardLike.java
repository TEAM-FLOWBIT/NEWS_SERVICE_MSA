package com.example.boardservice.domain.board.entity;

import com.example.boardservice.global.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "BOARD_LIKE")
@Where(clause = "deleted_at IS NULL")
public class BoardLike extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_LIKE_ID")
    private Long id;

    @JoinColumn(name="BOARD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Embedded
    private MemberId memberId;

    @Builder
    public BoardLike(Board board,MemberId memberId) {
        this.board = board;
        this.memberId = memberId;
    }
    public void setBoard(Board board) {
        this.board=board;
    }

    public void deleteBoardLike() {
        this.deletedAt = LocalDateTime.now();
    }
}
