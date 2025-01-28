package com.fortune.app.user.service;

import com.fortune.app.common.exception.CustomException;
import com.fortune.app.common.util.CacheNames;
import com.fortune.app.enums.ErrorCode;
import com.fortune.app.user.dto.UserRequestDto;
import com.fortune.app.user.repository.UserRepository;
import com.fortune.app.user.dto.UserDto;
import com.fortune.app.user.entity.User;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Caching(put = {
            @CachePut(value = CacheNames.USER, key = "T(com.fortune.app.common.util.CacheUtil).getUserCacheKey(#dto.userId)")
    }, evict = {
            @CacheEvict(value = CacheNames.USER_LIST, key = "T(com.fortune.app.common.util.CacheUtil).getUserListCacheKey()")}
    )
    public UserDto completeRegistration(UserRequestDto dto) {
        Optional<User> oauthUser = userRepository.findByUserIdQueryDSL(dto.getUserId());
        if (!oauthUser.isPresent()) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }

        User user = oauthUser.get();
        user.completeRegistration(dto.getNickname(), dto.getBirth());
        userRepository.save(user);

        return UserDto.mapToDto(user);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = CacheNames.USER, key = "T(com.fortune.app.common.util.CacheUtil).getUserCacheKey(#userId)")
    public UserDto getUser(Long userId) {
        User user = userRepository.findByUserIdQueryDSL(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserDto.mapToDto(user);
    }

    @Transactional
    @Caching(put = {
            @CachePut(value = CacheNames.USER, key = "T(com.fortune.app.common.util.CacheUtil).getUserCacheKey(#userId)")
    }, evict = {
            @CacheEvict(value = CacheNames.USER_LIST, key = "T(com.fortune.app.common.util.CacheUtil).getUserListCacheKey()")
    })
    public UserDto updateUser(Long userId, UserRequestDto dto) {
        User user = userRepository.findByUserIdQueryDSL(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        validateUserFields(dto, user);
        updateUserFields(user, dto);

        return UserDto.mapToDto(user);
    }

    private void updateUserFields(User user, UserRequestDto dto) {
        Optional.ofNullable(dto.getBirth()).ifPresent(user::setBirth);
        Optional.ofNullable(dto.getNickname()).ifPresent(user::setNickname);

        user.setUpdatedAt(LocalDateTime.now());
    }

    private void validateUserFields(UserRequestDto dto, User user) {
        if (dto.getNickname() != null && !dto.getNickname().equals(user.getNickname()) &&
                userRepository.existsByNicknameQueryDSL(dto.getNickname())) {
            throw new CustomException(ErrorCode.HAS_NICKNAME);
        }
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = CacheNames.USER, key = "T(com.fortune.app.common.util.CacheUtil).getUserCacheKey(#userId)"),
            @CacheEvict(value = CacheNames.USER_LIST, key = "T(com.fortune.app.common.util.CacheUtil).getUserListCacheKey()")
    })
    public void deleteUser(Long userId) {
        if (!userRepository.existsByUserIdQueryDSL(userId)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteByUserIdQueryDSL(userId);
    }


    @Cacheable(value = CacheNames.USER_LIST, key = "T(com.fortune.app.common.util.CacheUtil).getUserListCacheKey()")
    public List<UserDto> getUserList() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::mapToDto)
                .collect(Collectors.toList());
    }
}
