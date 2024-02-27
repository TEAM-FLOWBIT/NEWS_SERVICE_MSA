package com.example.boardservice.domain.board.entity;


import com.example.boardservice.global.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "BOARD_IMAGE")
@Where(clause = "deleted_at IS NULL")
public class BoardImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_IMAGE_ID")
    private Long id;

    @JoinColumn(name="BOARD_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Column(nullable = false)
    private String image;

    private Long memberId;

    @Builder
    public BoardImage(Long id, String image, Board board,Long memberId) {
        this.id = id;
        this.image = image;
        this.board = board;
        this.memberId=memberId;
    }

    public void setBoard(Board board) {
        this.board=board;
    }

    public void deleteBoardImage(){
        this.deletedAt= LocalDateTime.now();
    }
}
