package com.example.boardservice.domain.board.service.impl;
import com.example.boardservice.domain.board.dto.BoardSearchCondition;
import com.example.boardservice.domain.board.dto.request.CreateBoardRequestDto;
import com.example.boardservice.domain.board.dto.request.UpdateBoardRequestDto;
import com.example.boardservice.domain.board.dto.response.CreateBoardResponseDto;
import com.example.boardservice.domain.board.dto.response.ReadBoardListResponseDto;
import com.example.boardservice.domain.board.dto.response.UpdateBoardResponseDto;
import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.BoardImage;
import com.example.boardservice.domain.board.entity.BoardTags;
import com.example.boardservice.domain.board.exception.error.BoardNotFoundException;
import com.example.boardservice.domain.board.exception.error.UnAuthorizedException;
import com.example.boardservice.domain.board.repository.BoardRepository;
import com.example.boardservice.domain.board.repository.BoardTagRepository;
import com.example.boardservice.domain.board.service.BoardService;
import com.example.boardservice.domain.board.service.BoardTagService;
import com.example.boardservice.global.aws.AwsS3Service;
import com.example.boardservice.global.client.UserServiceClient;
import com.example.boardservice.global.client.dto.MemberInfoByMemberIdResponseDto;
import com.example.boardservice.global.client.dto.MemberInfoResponseDto;
import com.example.boardservice.global.common.CommonResDto;
import com.example.boardservice.global.tranlator.Translator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final AwsS3Service awsS3Service;
    private final UserServiceClient userServiceClient;
    private final BoardTagService boardTagService;
    private final BoardTagRepository boardTagRepository;



    @Override
    @Transactional
    public CreateBoardResponseDto createBoard(CreateBoardRequestDto createBoardRequestDto) {
        CommonResDto<MemberInfoResponseDto> memberInfo = userServiceClient.getMemberInfo();
        Long memberId=memberInfo.getData().getId();
        List<String> uploadedPaths = new ArrayList<>();
        List<BoardTags> boardTag = new ArrayList<>();


                Board board = createBoardRequestDto.toEntity(createBoardRequestDto, Translator.getMemberId(memberId));
        Board savedBoard = boardRepository.save(board);

        // 태그 단어 저장
        if(createBoardRequestDto.getBoardTags() != null && !createBoardRequestDto.getBoardTags().isEmpty()){
            boardTag = boardTagService.createBoardTag(savedBoard, createBoardRequestDto.getBoardTags());
        }


        if (createBoardRequestDto.getPictures() != null && !createBoardRequestDto.getPictures().isEmpty()) {
            // image 저장
            List<BoardImage> boardImageList = boardImageToEntity(uploadedPaths, savedBoard,memberId);
            for (BoardImage boardImage : boardImageList) {
                board.addBoardImage(boardImage);
            }
            //aws upload
            uploadedPaths = awsS3UploadImages(createBoardRequestDto.getPictures(), memberId);
        }
        return CreateBoardResponseDto.builder()
                .board(savedBoard)
                .memberInfoResponseDto(memberInfo.getData())
                .imagePaths(uploadedPaths)
                .boardTags(boardTag)
                .build();
    }

    @Override
    @Transactional
    public Page<ReadBoardListResponseDto> readBoardList(Pageable pageable, BoardSearchCondition boardSearchCondition) {
        Page<Board> boards = boardRepository.readBoardList(pageable,boardSearchCondition);

        List<ReadBoardListResponseDto> dtoList = boards.stream()
                .map(board -> {
                    // 멤버 정보 가져오기
                    CommonResDto<MemberInfoByMemberIdResponseDto> memberInfoResponseDto = userServiceClient.getMemberInfoByMemberId(board.getMemberId());
                    MemberInfoByMemberIdResponseDto memberInfoByMemberIdResponseDto = memberInfoResponseDto.getData();
                    // ReadBoardListResponseDto 빌더에 멤버 정보를 포함하여 객체 생성
                    return ReadBoardListResponseDto.builder()
                            .board(board)
                            .memberInfoByMemberIdResponseDto(memberInfoByMemberIdResponseDto) // 멤버 정보 추가
                            .build();
                })
                .collect(Collectors.toList());
        return new PageImpl<>(dtoList, pageable, boards.getSize());
    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId) {
        CommonResDto<MemberInfoResponseDto> memberInfo = userServiceClient.getMemberInfo();
        Board board = boardRepository.findById(boardId).orElseThrow(BoardNotFoundException::new);

        if(board.getMemberId().equals(memberInfo.getData().getId())){
            // Delete images
            List<BoardImage> boardImages = board.getBoardImages();
            boardImages.forEach(boardImage -> awsS3Service.deleteFile(boardImage.getImage()));
            // state값 변경시킴으로써 삭제
            board.deleteBoard();
        }else {
            throw new UnAuthorizedException("삭제할 권한이 없습니다");
        }
    }

    @Override
    @Transactional
    public UpdateBoardResponseDto updateBoard(UpdateBoardRequestDto updateBoardRequestDto,Long boardId) {
        CommonResDto<MemberInfoResponseDto> memberInfo = userServiceClient.getMemberInfo();
        Long memberId = memberInfo.getData().getId();
        List<String> uploadedPaths =new ArrayList<>();
        List<BoardTags> boardTags=new ArrayList<>();

        List<MultipartFile> uploadedFiles = updateBoardRequestDto.getPictures();
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException("해당 커뮤니티 글이 없습니다"));
        List<BoardImage> existingImages = board.getBoardImages();

        board.updateBoard(updateBoardRequestDto);
        if(updateBoardRequestDto.getPictures()!=null && !updateBoardRequestDto.getPictures().isEmpty()){
            updateBoardImages(board, uploadedFiles, existingImages, memberId);
        }

        if(updateBoardRequestDto.getBoardTags()!=null && !updateBoardRequestDto.getBoardTags().isEmpty()){
            boardTags = boardTagService.updateBoardTag(board, updateBoardRequestDto.getBoardTags());
        }

        return UpdateBoardResponseDto.builder()
                .board(board)
                .memberInfoResponseDto(memberInfo.getData())
                .imagePaths(uploadedPaths)
                .boardTags(boardTags)
                .build();
    }

    private static List<BoardImage> boardImageToEntity(List<String> uploadedPaths, Board savedBoard,Long memberId) {
        return uploadedPaths.stream().map(image -> BoardImage.builder()
                .board(savedBoard)
                .image(image)
                .memberId(Translator.getMemberId(memberId))
                .build()).collect(Collectors.toList());
    }

    private List<String> awsS3UploadImages(List<MultipartFile> pictures, Long memberId) {
        return pictures.stream().map(image -> {
            try {
                return awsS3Service.upload(image.getOriginalFilename(), image, String.valueOf(memberId));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }
    private List<String> updateBoardImages(Board board, List<MultipartFile> updatePictures, List<BoardImage> existingImages,
                                   Long memberId) {

        deleteAllExistingImages(board, existingImages);
        List<String> uploadedImages = awsS3UploadImages(updatePictures, memberId);
        uploadedImages.forEach(image -> {
            BoardImage boardImage = BoardImage.builder()
                    .image(image)
                    .memberId(Translator.getMemberId(memberId))
                    .build();
            board.addBoardImage(boardImage);
        });
        return uploadedImages;
    }
    private void deleteAllExistingImages(Board board, List<BoardImage> existingImages) {
        for (BoardImage existingImage : existingImages) {
            board.deleteBoardImage(existingImage);
        }
    }
}
