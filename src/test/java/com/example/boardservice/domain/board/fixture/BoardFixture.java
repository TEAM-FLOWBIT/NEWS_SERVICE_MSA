package com.example.boardservice.domain.board.fixture;


import com.example.boardservice.domain.board.dto.request.CreateBoardRequestDto;
import com.example.boardservice.domain.board.entity.*;
import com.example.boardservice.global.tranlator.Translator;

public class BoardFixture {



    public static Board createBoard(Long memberId){
        return Board.builder()
                .title("제목")
                .memberId(Translator.getMemberId(memberId))
                .content("내용")
                .like(new Like(0L))
                .boardCommentCount(new BoardCommentCount(0L))
                .build();
    }
    public static BoardComment createBoardComment(Board board,Long memberId){
        return BoardComment.builder()
                .board(board)
                .memberId(Translator.getMemberId(memberId))
                .content("댓글")
                .build();
    }
    public static BoardLike createBoardLike(Board board,Long memberId){
        return BoardLike.builder()
                .board(board)
                .memberId(Translator.getMemberId(memberId))
                .build();
    }





}
