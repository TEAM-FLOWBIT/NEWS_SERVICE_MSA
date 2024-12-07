package com.example.boardservice.domain.board.dto.request;

import com.example.boardservice.domain.board.entity.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class CreateBoardRequestDto {

    @NotEmpty(message = "내용을 입력해주세요.")
    private String title;
    @NotEmpty(message = "내용을 입력해주세요.")
    private String content;
    private BoardCategory boardCategory;
    @Size.List(value = @Size(min = 1, max = 3, message = "최대 3장까지 업로드할 수 있습니다."))
    List<MultipartFile> pictures;
    private String boardTags;


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

