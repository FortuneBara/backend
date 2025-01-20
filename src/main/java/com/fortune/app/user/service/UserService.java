package com.fortune.app.user.service;

import com.fortune.app.common.exception.CustomException;
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

    @Caching(put = {
            @CachePut(value = "user", key = "#root.targetClass.simpleName + '_' + #result.userId")
    }, evict = {
            @CacheEvict(value = "userList", key = "#root.targetClass.simpleName")}
    )
    public UserDto signUp(UserRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException(ErrorCode.HAS_EMAIL);
        }

        if (userRepository.existsByNickname(dto.getNickname())) {
            throw new CustomException(ErrorCode.HAS_NICKNAME);
        }

        User user = User.builder()
                .name(dto.getName())
                .birth(dto.getBirth())
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .provider(dto.getProvider())
                .providerUid(dto.getProviderUid())
                .accessToken(dto.getAccessToken())
                .refreshToken(dto.getRefreshToken())
                .build();
        User savedUser = userRepository.save(user);

        return UserDto.mapToDto(savedUser);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "user", key = "#root.targetClass.simpleName + '_' + #userId")
    public UserDto getUser(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserDto.mapToDto(user);
    }

    @Transactional
    @Caching(put = {
            @CachePut(value = "user", key = "#root.targetClass.simpleName + '_' + #userId")
    }, evict = {
            @CacheEvict(value = "userList", key = "#root.targetClass.simpleName")
    })
    public UserDto updateUser(Long userId, UserRequestDto dto) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        validateUserFields(dto, user);
        updateUserFields(user, dto);

        return UserDto.mapToDto(user);
    }

    private void updateUserFields(User user, UserRequestDto dto) {
        Optional.ofNullable(dto.getName()).ifPresent(user::setName);
        Optional.ofNullable(dto.getBirth()).ifPresent(user::setBirth);
        Optional.ofNullable(dto.getEmail()).ifPresent(user::setEmail);
        Optional.ofNullable(dto.getNickname()).ifPresent(user::setNickname);
        Optional.ofNullable(dto.getProvider()).ifPresent(user::setProvider);
        Optional.ofNullable(dto.getProviderUid()).ifPresent(user::setProviderUid);
        Optional.ofNullable(dto.getAccessToken()).ifPresent(user::setAccessToken);
        Optional.ofNullable(dto.getRefreshToken()).ifPresent(user::setRefreshToken);

        user.setUpdatedAt(LocalDateTime.now());
    }

    private void validateUserFields(UserRequestDto dto, User user) {
        if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(dto.getEmail())) {
            throw new CustomException(ErrorCode.HAS_EMAIL);
        }

        if (dto.getNickname() != null && !dto.getNickname().equals(user.getNickname()) &&
                userRepository.existsByNickname(dto.getNickname())) {
            throw new CustomException(ErrorCode.HAS_NICKNAME);
        }
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "user", key = "#root.targetClass.simpleName + '_' + #userId"),
            @CacheEvict(value = "userList", key = "#root.targetClass.simpleName")
    })
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        userRepository.deleteById(userId);
    }


    @Cacheable(value = "userList", key = "#root.targetClass.simpleName ")
    public List<UserDto> getUserList() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::mapToDto)
                .collect(Collectors.toList());
    }
}
