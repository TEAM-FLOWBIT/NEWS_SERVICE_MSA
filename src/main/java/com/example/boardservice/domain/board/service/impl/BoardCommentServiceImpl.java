package com.example.boardservice.domain.board.service.impl;

import com.example.boardservice.domain.board.dto.request.CreateBoardCommentRequestDto;
import com.example.boardservice.domain.board.dto.response.CreatedBoardCommentResponseDto;
import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.BoardComment;
import com.example.boardservice.domain.board.exception.error.BoardCommentNotFoundException;
import com.example.boardservice.domain.board.exception.error.BoardNotFoundException;
import com.example.boardservice.domain.board.exception.error.UnAuthorizedException;
import com.example.boardservice.domain.board.repository.BoardCommentRepository;
import com.example.boardservice.domain.board.repository.BoardRepository;
import com.example.boardservice.domain.board.service.BoardCommentService;
import com.example.boardservice.global.client.UserServiceClient;
import com.example.boardservice.global.client.dto.MemberInfoResponseDto;
import com.example.boardservice.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardCommentServiceImpl implements BoardCommentService {


    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;
    private final UserServiceClient userServiceClient;

    @Override
    @Transactional
    public CreatedBoardCommentResponseDto createBoardComment(CreateBoardCommentRequestDto createBoardCommentRequestDto) {

        CommonResDto<MemberInfoResponseDto> memberInfo = userServiceClient.getMemberInfo();
        Long boardId = createBoardCommentRequestDto.getBoardId();
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("게시글이 존재하지 않습니다"));

        BoardComment boardComment = createBoardCommentRequestDto.toEntity(memberInfo.getData().getId(), findBoard, createBoardCommentRequestDto.getContent());
        findBoard.addBoardComment(boardComment);
//        boardCommentRepository.save(boardComment);
        return CreatedBoardCommentResponseDto.builder()
                .boardComment(boardComment)
                .memberInfoResponseDto(memberInfo.getData())
                .build();
    }

    @Override
    @Transactional
    public void deleteBoardComment(Long commentId) {
        CommonResDto<MemberInfoResponseDto> memberInfo = userServiceClient.getMemberInfo();
        BoardComment boardComment = boardCommentRepository.findById(commentId).orElseThrow(() -> new BoardCommentNotFoundException("댓글을 찾지 못했습니다"));
        if(boardComment.getMemberId().equals(memberInfo.getData().getId())) {
            boardComment.getBoard().deleteBoardComment(boardComment);
        }else {
            throw new UnAuthorizedException("사용자의 댓글이 아닙니다.");
        }

    }
}
