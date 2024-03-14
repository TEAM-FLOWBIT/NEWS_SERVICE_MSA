package com.example.boardservice.domain.board.controller;


import com.example.boardservice.domain.board.dto.request.CreateBoardCommentRequestDto;
import com.example.boardservice.domain.board.dto.response.CreatedBoardCommentResponseDto;
import com.example.boardservice.domain.board.service.BoardCommentService;
import com.example.boardservice.global.common.CommonResDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/board/comment")
public class BoardCommentController {

    private final BoardCommentService boardCommentService;



    @Operation(
            description = "boardId를 통해 커뮤니티 댓글을 작성하는 API"
    )
    @PostMapping("")
    public ResponseEntity<CommonResDto<?>> createBoardComment(@RequestBody CreateBoardCommentRequestDto createBoardCommentRequestDto){

        CreatedBoardCommentResponseDto result = boardCommentService.createBoardComment(createBoardCommentRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1,"게시글 댓글작성 성공",result));
    }
    @Operation(
            description = "boardId를 통해 커뮤니티 댓글을 삭제하는 API"
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResDto<?>> deleteBoardComment(@PathVariable Long commentId){

        boardCommentService.deleteBoardComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"게시글 댓글삭제 성공",null));
    }


}
