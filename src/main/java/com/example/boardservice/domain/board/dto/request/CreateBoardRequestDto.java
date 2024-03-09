package com.example.boardservice.domain.board.dto.request;

import com.example.boardservice.domain.board.entity.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CreateBoardRequestDto {
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
    @NotEmpty(message = "카테고리를 지정해주세요.")
    private BoardCategory boardCategory;
    List<MultipartFile> pictures;

    public Board toEntity(CreateBoardRequestDto createBoardRequestDto, MemberId memberId) {
        return Board.builder()
                .content(createBoardRequestDto.getContent())
                .title(createBoardRequestDto.getTitle())
                .memberId(memberId)
                .like(new Like(0L))
                .boardCommentCount(new BoardCommentCount(0L))
                .boardCategory(createBoardRequestDto.getBoardCategory())
                .build();
    }
}

