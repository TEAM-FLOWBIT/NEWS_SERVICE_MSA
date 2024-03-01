package com.example.boardservice.domain.board.dto.request;

import com.example.boardservice.domain.board.entity.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CreateBoardRequestDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    @NotEmpty
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

