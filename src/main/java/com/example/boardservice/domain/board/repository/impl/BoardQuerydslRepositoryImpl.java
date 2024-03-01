package com.example.boardservice.domain.board.repository.impl;

import com.example.boardservice.domain.board.dto.BoardSearchCondition;
import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.board.repository.BoardQuerydslRepository;
import com.example.boardservice.global.config.Querydsl4RepositorySupport;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.example.boardservice.domain.board.entity.QBoard.board;


public class BoardQuerydslRepositoryImpl extends Querydsl4RepositorySupport implements BoardQuerydslRepository {


    public BoardQuerydslRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> readBoardList(Pageable pageable, BoardSearchCondition boardSearchCondition) {
        JPAQuery<Board> contentQuery = new JPAQueryFactory(getEntityManager()).
        selectFrom(board)
                .where(searchWordExpression(boardSearchCondition.getSearchword()))
                .offset(pageable.getOffset())
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = new JPAQueryFactory(getEntityManager())
                .select(board.count())
                .from(board)
                .where(searchWordExpression(boardSearchCondition.getSearchword()));

        return PageableExecutionUtils.getPage(contentQuery.fetch(),pageable, countQuery::fetchCount);
    }


    /**
     * 검색어 단어를 통한 동적검색
     */
    private BooleanExpression searchWordExpression(String searchWord) {

        return Optional.ofNullable(searchWord) //seachWord가 null이 아닌경우에 Optional로 감싸기
                .filter(word->!word.isEmpty()) // searchWord가 비어 있지 않은경우에만 map 함수
                .map(word-> Stream.of(board.content.containsIgnoreCase(word),
                                board.title.containsIgnoreCase(word))
                        .reduce(BooleanExpression::or) // 위 조건들을 OR 연산으로 묶음
                        .orElse(null))
                .orElse(null);//  // 만약 조건이 없으면 null 반환

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
