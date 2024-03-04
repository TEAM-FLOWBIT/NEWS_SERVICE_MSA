package com.example.boardservice.domain.board.controller;


import com.example.boardservice.domain.board.dto.BoardSearchCondition;
import com.example.boardservice.domain.board.dto.request.CreateBoardRequestDto;
import com.example.boardservice.domain.board.dto.request.UpdateBoardRequestDto;
import com.example.boardservice.domain.board.dto.response.CreateBoardResponseDto;
import com.example.boardservice.domain.board.dto.response.ReadBoardListResponseDto;
import com.example.boardservice.domain.board.dto.response.UpdateBoardResponseDto;
import com.example.boardservice.domain.board.service.BoardService;
import com.example.boardservice.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/board")
public class BoardController {


    private final BoardService boardService;

    @GetMapping("")
    public ResponseEntity<CommonResDto<?>> boardList(BoardSearchCondition boardSearchCondition,
                                                     Pageable pageable){

        Page<ReadBoardListResponseDto> result = boardService.readBoardList(pageable,boardSearchCondition);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"글 리스트 조회 성공",result));
    }
    @PostMapping("")
    public ResponseEntity<CommonResDto<?>> createBoard(@ModelAttribute CreateBoardRequestDto createBoardResquestDto){
        log.info("커뮤니티 글쓰기 API");
        CreateBoardResponseDto result = boardService.createBoard(createBoardResquestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1,"글 작성완료",result));
    }
    @PutMapping("/{boardId}")
    public ResponseEntity<CommonResDto<?>> updateBoard(@PathVariable Long boardId,@ModelAttribute UpdateBoardRequestDto updateBoardRequestDto){
        log.info("커뮤니티 업데이트 API");
        UpdateBoardResponseDto result = boardService.updateBoard(updateBoardRequestDto,boardId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"글 수정완료",result));
    }
    @DeleteMapping("/{boardId}")
    public ResponseEntity<CommonResDto<?>> deleteBoard(@PathVariable Long boardId){

        boardService.deleteBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"커뮤니티 삭제 성공",null));
    }
}
