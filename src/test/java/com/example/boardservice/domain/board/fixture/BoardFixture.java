package com.example.boardservice.domain.board.fixture;


import com.example.boardservice.domain.board.dto.request.CreateBoardRequestDto;

public class BoardFixture {

    public static CreateBoardRequestDto createBoardRequestDto = CreateBoardRequestDto.builder()
            .title("제목입니다")
            .content("내용입니다")
            .build();
}
