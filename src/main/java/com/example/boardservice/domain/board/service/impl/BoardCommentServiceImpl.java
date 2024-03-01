package com.example.boardservice.domain.board.service.impl;

import com.example.boardservice.domain.board.dto.request.CreateBoardCommentRequestDto;
import com.example.boardservice.domain.board.dto.response.CreatedBoardCommentResponseDto;
import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.BoardComment;
import com.example.boardservice.domain.board.entity.BoardCommentCount;
import com.example.boardservice.domain.board.entity.Like;
import com.example.boardservice.domain.board.exception.error.BoardCommentNotFoundException;
import com.example.boardservice.domain.board.exception.error.BoardNotFoundException;
import com.example.boardservice.domain.board.exception.error.UnAuthorizedException;
import com.example.boardservice.domain.board.repository.BoardCommentRepository;
import com.example.boardservice.domain.board.repository.BoardRepository;
import com.example.boardservice.domain.board.service.BoardCommentService;
import com.example.boardservice.global.client.UserServiceClient;
import com.example.boardservice.global.client.dto.MemberInfoResponseDto;
import com.example.boardservice.global.common.CommonResDto;
import com.example.boardservice.global.tranlator.MemberIdTranslator;
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
        Long memberId=memberInfo.getData().getId();
        Long boardId = createBoardCommentRequestDto.getBoardId();
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new BoardNotFoundException("게시글이 존재하지 않습니다"));

        BoardComment boardComment = createBoardCommentRequestDto.toEntity(MemberIdTranslator.getMemberId(memberId), board, createBoardCommentRequestDto.getContent());
        board.addBoardComment(boardComment);
        board.plusBoardCommentCount(new BoardCommentCount(board.getBoardCommentCount().getBoardCommentCount()).getBoardCommentCount());
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
        Board board = boardComment.getBoard();

        if(boardComment.getMemberId().equals(memberInfo.getData().getId())) {
            boardComment.getBoard().deleteBoardComment(boardComment);
            board.minusBoardCommentCount(new BoardCommentCount(board.getBoardCommentCount().getBoardCommentCount()).getBoardCommentCount());
        }else {
            throw new UnAuthorizedException("사용자의 댓글이 아닙니다.");
        }

    }
}
