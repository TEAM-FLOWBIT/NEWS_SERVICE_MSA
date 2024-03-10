package com.example.boardservice.domain.board.service.impl;

import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.BoardLike;
import com.example.boardservice.domain.board.entity.Like;
import com.example.boardservice.domain.board.entity.MemberId;
import com.example.boardservice.domain.board.exception.error.BoardNotFoundException;
import com.example.boardservice.domain.board.exception.error.DuplicateLikeException;
import com.example.boardservice.domain.board.exception.error.UnAuthorizedException;
import com.example.boardservice.domain.board.repository.BoardRepository;
import com.example.boardservice.domain.board.service.BoardLikeService;
import com.example.boardservice.global.client.UserServiceClient;
import com.example.boardservice.global.client.dto.MemberInfoResponseDto;
import com.example.boardservice.global.common.CommonResDto;
import com.example.boardservice.global.tranlator.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardLikeServiceImpl implements BoardLikeService {

    private final BoardRepository boardRepository;
    private final UserServiceClient userServiceClient;

    @Override
    @Transactional
    public Long boardLike(Long boardId) {
        CommonResDto<MemberInfoResponseDto> memberInfo = userServiceClient.getMemberInfo();
        Long memberId=memberInfo.getData().getId();

        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);

        BoardLike boardLike = BoardLike.builder()
                .memberId(Translator.getMemberId(memberId))
                .board(board)
                .build();
        // 이미 좋아요했는지 validationCheck
        if(board.validationCheckBoardLike(boardLike)){
            throw new DuplicateLikeException();
        }
        // boardLike 테이블에 추가함과 동시에 board테이블에 likecount 동기화
        board.addBoardLike(boardLike);
        board.plusLikeCount(new Like(board.getLike().getLikeCount()).getLikeCount());

        return board.getLike().getLikeCount();
    }

    @Override
    @Transactional
    public Long boardUnlike(Long boardId) {
        CommonResDto<MemberInfoResponseDto> memberInfo = userServiceClient.getMemberInfo();
        Long memberId=memberInfo.getData().getId();

        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);

        BoardLike boardLike = BoardLike.builder()
                .memberId(Translator.getMemberId(memberId))
                .board(board)
                .build();
        // boardLike 테이블에 state 변경과 동시에 board테이블에 likecount 동기화
        board.deleteBoardLike(new MemberId(memberId));
        if(!board.validationCheckBoardLike(boardLike)){
            throw new UnAuthorizedException();
        }
        board.minusLikeCount(board.getLike().getLikeCount());
        return board.getLike().getLikeCount();
    }
}
