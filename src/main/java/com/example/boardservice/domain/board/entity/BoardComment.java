package com.example.boardservice.domain.board.entity;
import com.example.boardservice.global.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "BOARD_COMMENT")
@Where(clause = "deleted_at IS NULL")
public class BoardComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_COMMENT_ID")
    private Long id;

    @Column(nullable = false)
    private String content;

    @JoinColumn(name="BOARD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private Long memberId;

    @Builder
    public BoardComment(Long id, String content, Board board, Long memberId) {
        this.id = id;
        this.content = content;
        this.board = board;
        this.memberId = memberId;
    }

    public void setBoard(Board board) {
        this.board=board;
    }

    public void deleteBoardImage(){
        this.deletedAt= LocalDateTime.now();
    }

}
