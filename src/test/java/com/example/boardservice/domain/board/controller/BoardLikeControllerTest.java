package com.example.boardservice.domain.board.controller;

import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.BoardLike;
import com.example.boardservice.domain.board.fixture.BoardFixture;
import com.example.boardservice.domain.board.repository.BoardRepository;
import com.example.boardservice.global.client.UserServiceClient;
import com.example.boardservice.global.client.dto.MemberInfoResponseDto;
import com.example.boardservice.global.common.CommonResDto;
import com.example.boardservice.util.ControllerTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardLikeControllerTest extends ControllerTestSupport {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    BoardRepository boardRepository;
    @MockBean
    private UserServiceClient userServiceClient;

    @BeforeEach
    void setUp() {
        CommonResDto<MemberInfoResponseDto> commonResDto = new CommonResDto<>();
        commonResDto.setData(MemberInfoResponseDto.builder()
                .id(1L)
                .nickname("testUser")
                .profile("/path/to/profile")
                .email("test@example.com")
                .build());

        given(userServiceClient.getMemberInfo()).willReturn(commonResDto);

    }

    @DisplayName("유저는 게시글에 좋아요를 할 수 있다.")
    @Test
    void createBoardLikeTest() throws Exception {
        //given
        Board board = BoardFixture.createBoard(1L);
        Board savedBoard = boardRepository.saveAndFlush(board);

        String url = "/api/v1/board/like?boardId="+savedBoard.getId();

        //when //then
        MvcResult mvcResult = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("좋아요를 하지않은 유저는 좋아요를 취소할 수 없다.")
    @Test
    void testCancelLikeUnavailableForNonLiker() throws Exception {
        //given
        Board board = BoardFixture.createBoard(1L);
        Board savedBoard = boardRepository.saveAndFlush(board);

        String url = "/api/v1/board/like?boardId="+savedBoard.getId();

        //when //then
        MvcResult mvcResult = mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @DisplayName("게시글에 좋아요를 한 유저는 좋아요를 취소할 수 있다.")
    @Test
    void createBoardUnLikeTest() throws Exception {
        //given
        Board board = BoardFixture.createBoard(1L);
        BoardLike boardLike = BoardFixture.createBoardLike(board, 1L);
        board.addBoardLike(boardLike);
        board.plusLikeCount(1L);

        Board savedBoard = boardRepository.saveAndFlush(board);

        String url = "/api/v1/board/like?boardId="+savedBoard.getId();

        //when //then
        MvcResult mvcResult = mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
}