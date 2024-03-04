package com.example.boardservice.domain.board.controller;


import com.example.boardservice.domain.board.dto.BoardSearchCondition;
import com.example.boardservice.domain.board.dto.response.ReadBoardListResponseDto;
import com.example.boardservice.domain.board.service.BoardLikeService;
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
@RequestMapping("/api/v1/board/like")
public class BoardLikeController {
    private final BoardLikeService boardLikeService;

    @GetMapping("")
    public ResponseEntity<CommonResDto<?>> boardLike(@RequestParam(value="boardId") Long boardId){

        Long result = boardLikeService.boardLike(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"좋아요 성공",result));
    }
    @DeleteMapping("")
    public ResponseEntity<CommonResDto<?>> boardUnLike(@RequestParam(value="boardId") Long boardId){
        Long result = boardLikeService.boardUnlike(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"좋아요 성공",result));
    }
}
