package com.fortune.app.user.repository;

import com.fortune.app.user.entity.QUser;
import com.fortune.app.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Optional<User> findByUserIdQueryDSL(Long userId) {
        QUser user = QUser.user;
        return Optional.ofNullable(
                queryFactory.selectFrom(user)
                        .where(user.userId.eq(userId))
                        .fetchOne()
        );
    }

    @Override
    public boolean existsByNicknameQueryDSL(String nickname) {
        QUser user = QUser.user;
        return queryFactory.select(user.userId)
                .from(user)
                .where(user.nickname.eq(nickname))
                .fetchFirst() != null;
    }

    @Override
    public Optional<User> findByEmailQueryDSL(String email) {
        QUser user = QUser.user;
        return Optional.ofNullable(
                queryFactory.selectFrom(user)
                        .where(user.email.eq(email))
                        .fetchOne()
        );
    }

    @Override
    public boolean existsByUserIdQueryDSL(Long userId) {
        QUser user = QUser.user;
        return queryFactory.select(user.userId)
                .from(user)
                .where(user.userId.eq(userId))
                .fetchFirst() != null;
    }

    @Override
    @Transactional
    public void deleteByUserIdQueryDSL(Long userId) {
        QUser user = QUser.user;
        queryFactory.delete(user)
                .where(user.userId.eq(userId))
                .execute();
    }
}
