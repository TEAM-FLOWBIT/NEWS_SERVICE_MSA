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
public class UpdateBoardRequestDto {
    @NotEmpty(message = "제목을 입력해주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
    @Column(nullable = false)
    List<MultipartFile> pictures;
    public Board toEntity(UpdateBoardRequestDto updateBoardRequestDto, MemberId memberId) {
        return Board.builder()
                .content(updateBoardRequestDto.getContent())
                .title(updateBoardRequestDto.getTitle())
                .memberId(memberId)
                .build();
    }
}
