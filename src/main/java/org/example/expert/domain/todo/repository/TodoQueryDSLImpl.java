package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.todo.dto.request.TodoSearchDTO;
import org.example.expert.domain.todo.dto.response.TodoSearchResponseDTO;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.*;
import static org.example.expert.domain.user.entity.QUser.*;


public class TodoQueryDSLImpl implements TodoQueryDSL {

    private final JPAQueryFactory query;

    public TodoQueryDSLImpl(JPAQueryFactory query) {
        this.query = query;
    }


    @Override
    public Optional<Todo> findByTodoIdWithUser(Long todoId) {
        //상세보기
        return Optional.ofNullable(query.selectFrom(todo)
                .join(todo.user, user).fetchJoin()
                .where(todo.id.eq(todoId))
                .fetchOne());
    }

    @Override
    public Page<TodoSearchResponseDTO> search(TodoSearchDTO req, Pageable pageable) {

            QTodo todo = QTodo.todo;
            QManager manager = QManager.manager;
            QComment comment= QComment.comment;
        //목록 + count
        List<TodoSearchResponseDTO> content = query
                .select(Projections.constructor(
                        TodoSearchResponseDTO.class,
                        todo.id,
                        todo.title,
                        manager.id.countDistinct(),
                        comment.id.countDistinct()
                ))
                .from(todo)
                .leftJoin(manager).on(manager.todo.eq(todo))
                .leftJoin(comment).on(comment.todo.eq(todo))
                .where(
                        titleLike(req.keyword()),
                        createdAfter(req.startDate()),
                        createdBefore(req.endDate()),
                        managerNicknameLike(req.managerNickName())
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        //카운트
        long total = query
                .select(todo.id.count())
                .from(todo)
                .leftJoin(manager).on(manager.todo.eq(todo))
                .where(
                        titleLike(req.keyword()),
                        createdAfter(req.startDate()),
                        createdBefore(req.endDate()),
                        managerNicknameLike(req.managerNickName())
                ).fetchOne();

        return new PageImpl<>(content, pageable, total);
    }


    private BooleanExpression titleLike(String keyword){
        return StringUtils.hasText(keyword) ? todo.title.contains(keyword) : null;
    }

    private BooleanExpression createdAfter(LocalDate start) {
        return start != null ? QTodo.todo.createdAt.goe(start.atStartOfDay()) : null;
    }
    private BooleanExpression createdBefore(LocalDate end) {
        return end != null ? QTodo.todo.createdAt.loe(end.atTime(23,59,59)) : null;
    }

    private BooleanExpression managerNicknameLike(String nickName){
        if(!StringUtils.hasText(nickName)) return null;
        return QManager.manager.user.userNickName.contains(nickName);
    }
}
