package com.example.boardservice.domain.board.repository.impl;

import com.example.boardservice.domain.board.dto.BoardSearchCondition;
import com.example.boardservice.domain.board.entity.*;
import com.example.boardservice.domain.board.repository.BoardQuerydslRepository;
import com.example.boardservice.global.config.Querydsl4RepositorySupport;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.boardservice.domain.board.entity.QBoard.board;
import static com.example.boardservice.domain.board.entity.QBoardTags.boardTags;
import static com.example.boardservice.domain.board.entity.QTag.tag;


public class BoardQuerydslRepositoryImpl extends Querydsl4RepositorySupport implements BoardQuerydslRepository {



    public BoardQuerydslRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> readBoardList(Pageable pageable, BoardSearchCondition boardSearchCondition) {

        LocalDateTime[] weekRange = showDataForPastWeeks(boardSearchCondition);

        JPAQuery<Board> contentQuery = new JPAQueryFactory(getEntityManager()).
        selectFrom(board)
                .where(searchWordExpression(boardSearchCondition.getSearchword())
                        ,categoryExpression(boardSearchCondition.getCategory())
                        ,boardTagExpression(boardSearchCondition.getBoardTag()))
                .offset(pageable.getOffset())
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = new JPAQueryFactory(getEntityManager())
                .select(board.count())
                .from(board)
                .where(searchWordExpression(boardSearchCondition.getSearchword())
                        ,categoryExpression(boardSearchCondition.getCategory())
                        ,boardTagExpression(boardSearchCondition.getBoardTag()));

        if (weekRange[0] != null && weekRange[1] != null) {
            contentQuery = contentQuery.where(board.updatedAt.between(weekRange[0], weekRange[1]));
        }

        return PageableExecutionUtils.getPage(contentQuery.fetch(),pageable, countQuery::fetchCount);
    }

    private static LocalDateTime[] showDataForPastWeeks(BoardSearchCondition boardSearchCondition) {
        LocalDateTime[] weekRange = new LocalDateTime[2];
        if (boardSearchCondition.getSetDataForPastWeeks()!=null) {
            Integer setDataForPastWeeks = boardSearchCondition.getSetDataForPastWeeks();
            weekRange[0] = LocalDateTime.now().minusWeeks(setDataForPastWeeks);
            weekRange[1] = LocalDateTime.now();
        } else {
            weekRange[0] = null;
            weekRange[1] = null;
        }
        return weekRange;
    }

    /**
     * 주간 - 좋아요 순
     */
    @Override
    public Page<Board> readTopWeeklyLikedBoards(Pageable pageable){
        LocalDateTime startOfWeek = LocalDateTime.now().minusWeeks(1);
        LocalDateTime endOfWeek = LocalDateTime.now();

        JPAQuery<Board> query = new JPAQueryFactory(getEntityManager())
                .selectFrom(board)
                .where(board.createdAt.between(startOfWeek, endOfWeek))
                .orderBy(board.like.likeCount.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = new JPAQueryFactory(getEntityManager())
                .select(board.count())
                .from(board)
                .where(board.createdAt.between(startOfWeek, endOfWeek));

        return PageableExecutionUtils.getPage(query.fetch(), pageable, countQuery::fetchCount);
    }


    /**
     * 검색어 단어를 통한 동적검색
     */
    private BooleanExpression searchWordExpression(String searchWord) {

        return Optional.ofNullable(searchWord)
                .filter(word->!word.isEmpty())
                .map(word-> Stream.of(board.content.containsIgnoreCase(word),
                                board.title.containsIgnoreCase(word))
                        .reduce(BooleanExpression::or)
                        .orElse(null))
                .orElse(null);//

    }

    /**
     *
     * 해당 카테고리에 맞는 게시글
     */
    private BooleanExpression categoryExpression(String category) {

        return Optional.ofNullable(category)
                .filter(word->!word.isEmpty())
                .map(word-> Stream.of(board.boardCategory.stringValue().containsIgnoreCase(category))
                        .reduce(BooleanExpression::or)
                        .orElse(null))
                .orElse(null);

    }
    /**
     * 해당 태그에 맞는 게시글
     */
    private BooleanExpression boardTagExpression(String boardTag) {
        if (boardTag == null || boardTag.isEmpty()) {
            return null;
        }

        List<Board> boardsWithTag = getQueryFactory().selectFrom(board)
                .join(board.boardTags, boardTags)
                .join(boardTags.tag, tag)
                .where(tag.word.eq(boardTag))
                .fetch();

        List<Long> boardIds = boardsWithTag.stream()
                .map(Board::getId)
                .collect(Collectors.toList());


        return board.id.in(boardIds);
    }





    /**
     * 동적 orderby
     */

    private List<OrderSpecifier> getOrderSpecifier(Sort sort){
        List<OrderSpecifier> orders=new ArrayList<>();

        sort.stream().forEach(order->{
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            PathBuilder<Board> orderByExpression = new PathBuilder<>(Board.class, "board");
            orders.add(new OrderSpecifier(direction,orderByExpression.get(property)));
        });
        return orders;
    }
}
