package com.example.boardservice.domain.board.dto.request;

import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.MemberId;
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
    List<MultipartFile> pictures;

    public Board toEntity(CreateBoardRequestDto createBoardRequestDto, MemberId memberId) {
        return Board.builder()
                .content(createBoardRequestDto.getContent())
                .title(createBoardRequestDto.getTitle())
                .memberId(memberId)
                .build();
    }
}

