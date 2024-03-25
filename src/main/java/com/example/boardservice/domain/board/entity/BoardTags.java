package com.example.boardservice.domain.board.entity;

import com.example.boardservice.global.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted_at IS NULL")
@AllArgsConstructor
@Builder
public class BoardTags extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "BoardTags_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    private Tag tag;
}