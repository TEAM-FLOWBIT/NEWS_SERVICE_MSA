package com.example.boardservice.domain.news.repository.impl;

import com.example.boardservice.domain.board.entity.Board;
import com.example.boardservice.domain.news.dto.NewsSearchCondition;
import com.example.boardservice.domain.news.entity.News;
import com.example.boardservice.domain.news.entity.QNews;
import com.example.boardservice.domain.news.repository.NewsQuerydslRepository;
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
import static com.example.boardservice.domain.news.entity.QNews.news;

public class NewsQuerydslRepositoryImpl extends Querydsl4RepositorySupport implements NewsQuerydslRepository {
    public NewsQuerydslRepositoryImpl() {
        super(News.class);
    }

    @Override
    public Page<News> readBoardList(NewsSearchCondition newsSearchCondition, Pageable pageable) {
        JPAQuery<News> contentQuery = new JPAQueryFactory(getEntityManager()).
                selectFrom(news)
                .where(newsTagExpression(newsSearchCondition.getTag()),
                        searchWordExpression(newsSearchCondition.getSearchword()))
                .offset(pageable.getOffset())
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize());

        JPAQuery<Long> countQuery = new JPAQueryFactory(getEntityManager())
                .select(news.count())
                .from(news)
                .where(newsTagExpression(newsSearchCondition.getTag()),
                        searchWordExpression(newsSearchCondition.getSearchword()))
                .offset(pageable.getOffset())
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize());
        return PageableExecutionUtils.getPage(contentQuery.fetch(),pageable, countQuery::fetchOne);
    }


    /**
     *
     * 해당 카테고리에 맞는 게시글
     */
    private BooleanExpression newsTagExpression(String tag) {
        //seachWord가 null이 아닌경우에 Optional로 감싸기
        // searchWord가 비어 있지 않은경우에만 map 함수
        return Optional.ofNullable(tag) //seachWord가 null이 아닌경우에 Optional로 감싸기
                .filter(word -> !word.isEmpty()).flatMap(word -> Optional.of(news.tag.contains(tag)))
                .orElse(null);//  // 만약 조건이 없으면 null 반환

    }


    /**
     * 검색어 단어를 통한 동적검색
     */
    private BooleanExpression searchWordExpression(String searchWord) {

        return Optional.ofNullable(searchWord)
                .filter(word->!word.isEmpty())
                .map(word-> Stream.of(news.title.containsIgnoreCase(word),
                                news.tag.containsIgnoreCase(word),
                                news.description.containsIgnoreCase(word),
                                news.link.containsIgnoreCase(word))
                        .reduce(BooleanExpression::or)
                        .orElse(null))
                .orElse(null);//

    }


    /**
     * 동적 orderby
     */

    private List<OrderSpecifier> getOrderSpecifier(Sort sort){
        List<OrderSpecifier> orders=new ArrayList<>();

        sort.stream().forEach(order->{
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            PathBuilder<News> orderByExpression = new PathBuilder<>(News.class, "news");
            orders.add(new OrderSpecifier(direction,orderByExpression.get(property)));
        });
        return orders;
    }
}
