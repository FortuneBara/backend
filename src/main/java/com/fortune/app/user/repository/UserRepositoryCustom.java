package com.fortune.app.user.repository;

import com.fortune.app.user.entity.User;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<User> findByUserIdQueryDSL(Long userId);

    Optional<User> findByEmailQueryDSL(String email);

    boolean existsByNicknameQueryDSL(String nickname);

    boolean existsByUserIdQueryDSL(Long userId);

    void deleteByUserIdQueryDSL(Long userId);
}
