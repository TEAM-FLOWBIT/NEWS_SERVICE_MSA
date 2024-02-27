package com.example.boardservice.domain.board.controller;
import com.example.boardservice.domain.board.dto.request.CreateBoardCommentRequestDto;
import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.BoardComment;
import com.example.boardservice.domain.board.repository.BoardCommentRepository;
import com.example.boardservice.domain.board.repository.BoardRepository;
import com.example.boardservice.domain.board.service.BoardService;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardCommentControllerTest extends ControllerTestSupport {


    @Autowired
    BoardService boardService;
    @Autowired
    BoardCommentRepository boardCommentRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceClient userServiceClient;

    private static final Long initalUserMemberId=1L;
    @BeforeEach
    void setUp() {
        CommonResDto<MemberInfoResponseDto> commonResDto = new CommonResDto<>();
        commonResDto.setData(MemberInfoResponseDto.builder()
                .id(initalUserMemberId)
                .nickname("testUser")
                .profile("/path/to/profile")
                .email("test@example.com")
                .build());

        given(userServiceClient.getMemberInfo()).willReturn(commonResDto);

    }

    @DisplayName("로그인을 한 유저는 커뮤니티 게시글에 댓글을 작성 할 수 있다.")
    @Test
    void createBoardComment() throws Exception {
        //given
        Board board = Board.builder()
                .title("제목")
                .memberId(initalUserMemberId)
                .content("내용")
                .build();
        Board savedBoard = boardRepository.saveAndFlush(board);

        String url = "/api/v1/board/comment";
        CreateBoardCommentRequestDto createBoardCommentRequestDto= CreateBoardCommentRequestDto.builder()
                .boardId(savedBoard.getId())
                .content("댓글입니다")
                .build();

        //when //then
        MvcResult mvcResult = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBoardCommentRequestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

    }
    @DisplayName("로그인을 한 유저는 커뮤니티 게시글에 자신이 등록한 댓글을 삭제 할 수 있다.")
    @Test
    void deleteBoardComment() throws Exception {


        //given
        Board board = Board.builder()
                .title("제목")
                .memberId(initalUserMemberId)
                .content("내용")
                .build();

        BoardComment boardComment = BoardComment.builder()
                .board(board)
                .memberId(initalUserMemberId)
                .content("댓글")
                .build();


        boardRepository.saveAndFlush(board);
        BoardComment savedBoardComment = boardCommentRepository.saveAndFlush(boardComment);
        String url = "/api/v1/board/comment/"+savedBoardComment.getId();

        //when //then
        MvcResult mvcResult = mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


    }
    @DisplayName("사용자는 다른유저의 댓글을 삭제할 수 없다.")
    @Test
    void loggedInUserCannotDeleteOtherUserBoardComment() throws Exception {
        //given
        Board board= Board.builder()
                .title("제목")
                .memberId(initalUserMemberId)
                .content("내용")
                .build();
        BoardComment boardComment = BoardComment.builder()
                .board(board)
                .memberId(initalUserMemberId+1L) //another user
                .content("댓글")
                .build();
        boardRepository.saveAndFlush(board);
        boardCommentRepository.saveAndFlush(boardComment);


        String url = "/api/v1/board/comment/"+boardComment.getId();
        //when //then
        MvcResult mvcResult = mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();



    }

}