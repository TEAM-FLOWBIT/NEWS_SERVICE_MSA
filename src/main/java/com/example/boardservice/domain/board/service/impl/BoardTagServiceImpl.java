package com.example.boardservice.domain.board.service.impl;
import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.entity.BoardTags;
import com.example.boardservice.domain.board.entity.Tag;
import com.example.boardservice.domain.board.repository.BoardTagRepository;
import com.example.boardservice.domain.board.repository.TagRepository;
import com.example.boardservice.domain.board.service.BoardTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BoardTagServiceImpl implements BoardTagService {


    private final TagRepository tagRepository;
    private final BoardTagRepository boardTagRepository;

    @Override
    public List<BoardTags> createBoardTag(Board board, String  tags) {
        String[] cleanedTags = tags.replaceAll("\\[|\\]", "").split(",");

        // 저장되지 않은 태그만 필터링하여 저장
        Set<Tag> allTags = Arrays.stream(cleanedTags)
                .map(String::trim)
                .filter(tag -> !tagRepository.existsByWord(tag))
                .map(unsavedTag -> tagRepository.save(Tag.builder().word(unsavedTag).build()))
                .collect(Collectors.toSet());

        // 이미 저장된 태그 가져오기
        allTags.addAll(tagRepository.findByWordIn(Arrays.asList(cleanedTags)));

        // BoardTags 생성
        List<BoardTags> boardTagsList = allTags.stream().map(tag -> BoardTags.builder()
                        .tag(tag)
                        .board(board)
                        .build())
                .collect(Collectors.toList());

        return boardTagRepository.saveAll(boardTagsList);
    }

    @Override
    @Transactional
    public List<BoardTags> updateBoardTag(Board board, String tags) {
        boardTagRepository.deleteAllByBoard_Id(board.getId());
        List<BoardTags> boardTag = createBoardTag(board, tags);
        return boardTag;
    }
}
