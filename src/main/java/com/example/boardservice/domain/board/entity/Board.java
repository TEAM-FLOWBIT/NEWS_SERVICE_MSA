package com.example.boardservice.domain.board.entity;

import com.example.boardservice.domain.board.dto.request.UpdateBoardRequestDto;
import com.example.boardservice.domain.board.exception.error.UnAuthorizedException;
import com.example.boardservice.global.entity.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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

    @Embedded
    private Like like;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoardImage> boardImages= new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoardLike> boardLikes= new ArrayList<>();


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
        if(boardComment!=null){
            boardComment.setBoard(this);
            this.boardComments.add(boardComment);
        }
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

    /**
     * boardLike
     */
    public void addBoardLike(BoardLike boardLike) {
        if(boardLike!=null){
            boardLike.setBoard(this);
            this.boardLikes.add(boardLike);
        }
    }
    public void deleteBoardLike(MemberId memberId) {
        Optional<BoardLike> likedBoardLike = this.boardLikes.stream()
                .filter(like -> like.getMemberId().equals(memberId))
                .findFirst();
        likedBoardLike.orElseThrow(() -> new UnAuthorizedException("좋아요를 한 적이 없습니다."))
                .deleteBoardLike();
        likedBoardLike.ifPresent(BoardLike::deleteBoardLike);
    }
    public boolean validationCheckBoardLike(BoardLike boardLike){
        return this.boardLikes.stream()
                .anyMatch(like -> like.getMemberId().equals(boardLike.getMemberId()));
    }

    /**
     * boardLike 동기화
     */
    public void plusLikeCount(Long likeCount) {
        this.like.plusLikeCount(likeCount);
    }
    public void minusLikeCount(Long likeCount) {
        this.like.minusLikeCount(likeCount);
    }




    public Long getMemberId(){
        return this.memberId.getId();
    }
}
