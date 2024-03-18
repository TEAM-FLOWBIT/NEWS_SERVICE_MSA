package com.example.boardservice.domain.board.controller;


import com.example.boardservice.domain.board.dto.BoardSearchCondition;
import com.example.boardservice.domain.board.dto.request.CreateBoardRequestDto;
import com.example.boardservice.domain.board.dto.request.UpdateBoardRequestDto;
import com.example.boardservice.domain.board.dto.response.CreateBoardResponseDto;
import com.example.boardservice.domain.board.dto.response.ReadBoardListResponseDto;
import com.example.boardservice.domain.board.dto.response.UpdateBoardResponseDto;
import com.example.boardservice.domain.board.service.BoardService;
import com.example.boardservice.global.common.CommonResDto;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/board")
public class BoardController {


    private final BoardService boardService;

    @Operation(
            description = "커뮤니티 리스트를 가져오는 API 페이징 가능 동저구커리 가능"
    )
    @GetMapping("")
    public ResponseEntity<CommonResDto<?>> boardList(BoardSearchCondition boardSearchCondition,
                                                     Pageable pageable){

        Page<ReadBoardListResponseDto> result = boardService.readBoardList(pageable,boardSearchCondition);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"글 리스트 조회 성공",result));
    }

    @Operation(
            description = "커뮤니티를 생성하는 API"
    )
    @PostMapping("")
    public ResponseEntity<CommonResDto<?>> createBoard(@Valid @ModelAttribute CreateBoardRequestDto createBoardResquestDto, BindingResult bindingResult){
        log.info("커뮤니티 글쓰기 API");
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResDto<>(0,errorMessage,null));
        }
        CreateBoardResponseDto result = boardService.createBoard(createBoardResquestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResDto<>(1,"글 작성완료",result));
    }
    @Operation(
            description = "커뮤니티 업데이트하는 API"
    )
    @PutMapping("/{boardId}")
    public ResponseEntity<CommonResDto<?>> updateBoard(@PathVariable Long boardId, @ModelAttribute UpdateBoardRequestDto updateBoardRequestDto, BindingResult bindingResult){
        log.info("커뮤니티 업데이트 API");
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
                    .collect(Collectors.joining(", "));

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CommonResDto<>(0,errorMessage,null));
        }
        UpdateBoardResponseDto result = boardService.updateBoard(updateBoardRequestDto,boardId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"글 수정완료",result));
    }

    @Operation(
            description = "커뮤니티 삭제하는 API"
    )
    @DeleteMapping("/{boardId}")
    public ResponseEntity<CommonResDto<?>> deleteBoard(@PathVariable Long boardId){

        boardService.deleteBoard(boardId);
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1,"커뮤니티 삭제 성공",null));
    }
}
