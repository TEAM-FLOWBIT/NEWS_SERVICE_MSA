package com.example.boardservice.domain.board.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardSearchCondition {

    private String searchword;
    private Integer setDataForPastWeeks;
}
