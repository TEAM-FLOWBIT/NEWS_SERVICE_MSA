package com.example.boardservice.domain.board.entity;

import com.example.boardservice.domain.board.dto.request.UpdateBoardRequestDto;
import com.example.boardservice.global.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "BOARD")
@Where(clause = "deleted_at IS NULL")
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long id;
    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false, length = 1000)
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoardComment> boardComments=new ArrayList<>();

    @Embedded
    private MemberId memberId;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoardImage> boardImages= new ArrayList<>();

    /**
     * board
     */
    public void updateBoard(UpdateBoardRequestDto updateBoardRequestDto) {
        this.title=updateBoardRequestDto.getTitle();
        this.content=updateBoardRequestDto.getContent();
    }
    public void deleteBoard() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * boardComment
     */
    public void addBoardComment(BoardComment boardComment) {
        boardComment.setBoard(this);
        System.out.println(this.id);
        this.boardComments.add(boardComment);
    }
    public void deleteBoardComment(BoardComment boardComment) {
        boardComment.deleteBoardImage();
    }
    /**
     * boardImage
     */
    public void addBoardImage(BoardImage boardImage) {
        if (boardImage!=null) {
            boardImage.setBoard(this);
            this.boardImages.add(boardImage);
        }
    }

    public void deleteBoardImage(BoardImage boardImage){
        boardImage.deleteBoardImage();
    }

    public Long getMemberId(){
        return this.memberId.getId();
    }
}
