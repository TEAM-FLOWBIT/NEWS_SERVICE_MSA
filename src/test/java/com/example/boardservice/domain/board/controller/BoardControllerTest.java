package com.example.boardservice.domain.board.controller;
import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.repository.BoardCommentRepository;
import com.example.boardservice.domain.board.repository.BoardRepository;
import com.example.boardservice.domain.board.service.BoardService;
import com.example.boardservice.global.client.UserServiceClient;
import com.example.boardservice.global.client.dto.MemberInfoResponseDto;
import com.example.boardservice.global.common.CommonResDto;
import com.example.boardservice.global.tranlator.Translator;
import com.example.boardservice.util.ControllerTestSupport;
import com.example.boardservice.util.ImageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.HttpClientErrorException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import static org.jsoup.helper.HttpConnection.MULTIPART_FORM_DATA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class BoardControllerTest extends ControllerTestSupport {



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


    @DisplayName("로그인을 한 유저는 커뮤니티에 게시글을 이미지 없이 작성할 수 있다.")
    @Test
    void creaetBoard() throws Exception {
        // Create a mock board
        Board board = Board.builder()
                .title("제목")
                .memberId(Translator.getMemberId(1L))
                .content("내용")
                .build();
        String url = "/api/v1/board";
        //when //then
        MvcResult mvcResult = mockMvc.perform(
                        multipart(url)
                                .param("title","제목입니다")
                                .param("content","내용입니다")
                                .param("boardCategory","BITCOIN")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }
    @DisplayName("로그인을 한 유저는 커뮤니티에 게시글을 이미지와 함께 작성할 수 있다.")
    @Test
    void creaetBoardWithImageFile() throws Exception {

        //given

        List<MockMultipartFile> multiFiles = List.of(
                ImageUtil.generateMockImageFile("subFiles"),
                ImageUtil.generateMockImageFile("subFiles")
        );


        String url = "/api/v1/board";
        //when //then
        MvcResult mvcResult = mockMvc.perform(
                        multipart(url)
                                .file(multiFiles.get(0))
                                .file(multiFiles.get(1))
                                .param("title","제목입니다")
                                .param("content","내용입니다")
                                .param("boardCategory","BITCOIN")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();

    }

    @DisplayName("로그인을 한 유저는 커뮤니티에 게시글을과함께 태그를 작성할 수 있다.")
    @Test
    void creaetBoardWithTag() throws Exception {
        // Create a mock board
        Board board = Board.builder()
                .title("제목")
                .memberId(Translator.getMemberId(1L))
                .content("내용")
                .build();
        String url = "/api/v1/board";
        //when //then
        MvcResult mvcResult = mockMvc.perform(

                        multipart(url)
                                .param("title","제목입니다")
                                .param("content","내용입니다")
                                .param("boardCategory","BITCOIN")
                                .param("boardTags","[테스트,테스트1,테스트2]")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @DisplayName("로그인을 한 유저는 커뮤니티에 게시글을 수정할 수 있다.")
    @Test
    void updateBoard() throws Exception {
        // Create a mock board
        Board board = Board.builder()
                .title("제목")
                .memberId(Translator.getMemberId(1L))
                .content("내용")
                .build();

        Board savedBoard = boardRepository.saveAndFlush(board);

        String url = "/api/v1/board/"+savedBoard.getId();
        //when //then

        MvcResult mvcResult = mockMvc.perform(
                        MockMvcRequestBuilders.put(url)
                                .param("title","제목입니다")
                                .param("content","내용입니다")
                                .param("boardCategory","BITCOIN")
                                .param("boardTags","[테스트,테스트1,테스트2]")
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @DisplayName("로그인을 한 유저는 자신이 등록한 커뮤니티를 삭제 할 수 있다.")
    @Test
    void deleteBoard() throws Exception {

        //given
        Board board= Board.builder()
                .title("제목")
                .memberId(Translator.getMemberId(1L))
                .content("내용")
                .build();
        boardRepository.saveAndFlush(board);
        String url = "/api/v1/board/"+board.getId();
        //when //then
        MvcResult mvcResult = mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }
    @DisplayName("로그인을 하지않은 사용자는 게시글을 작성할 수 없다.")
    @Test
    void unauthenticatedUserCannotCreateBoard() throws Exception {

        //given
        String url = "/api/v1/board";

        given(userServiceClient.getMemberInfo()).willThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED));
        //when //then
        mockMvc.perform(post(url)
                        .contentType(MULTIPART_FORM_DATA)
                        .param("title", "제목입니다")
                        .param("content", "내용입니다"))
                .andDo(print())
                .andExpect(status().isUnauthorized());


    }

    @DisplayName("사용자는 다른유저의 게시글을 삭제할 수 없다.")
    @Test
    void loggedInUserCannotDeleteOtherUserBoard() throws Exception {
        //given
        Board board= Board.builder()
                .title("제목")
                .memberId(Translator.getMemberId(2L)) // another user
                .content("내용")
                .build();
        boardRepository.saveAndFlush(board);
        String url = "/api/v1/board/"+board.getId();
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