package com.fortune.app.user.service.integration;

import com.fortune.app.common.exception.CustomException;
import com.fortune.app.enums.ErrorCode;
import com.fortune.app.user.dto.UserDto;
import com.fortune.app.user.dto.UserRequestDto;
import com.fortune.app.user.entity.User;
import com.fortune.app.user.repository.UserRepository;
import com.fortune.app.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceMockitoTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .userId(1L)
                .name("haru")
                .email("haru.every@example.com")
                .provider("Google")
                .providerUid("1234")
                .isRegistered(true)
                .accessToken("access_token")
                .refreshToken("refresh_token")
                .birth(new java.sql.Date(2000, 1, 1))
                .nickname("harus")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetUser_Success() {
        Long userId = 1L;
        when(userRepository.findByUserIdQueryDSL(userId)).thenReturn(Optional.of((mockUser)));

        UserDto result = userService.getUser(userId);

        assertNotNull(result);
        assertEquals(mockUser.getUserId(), result.getUserId());
        assertEquals(mockUser.getNickname(), result.getNickname());
        verify(userRepository, times(1)).findByUserIdQueryDSL(userId);
    }

    @Test
    void testGetUser_UserNotFound() {
        Long userId = 1L;
        when(userRepository.findByUserIdQueryDSL(userId)).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> userService.getUser(userId));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testCompleteRegistration_Success() {
        UserRequestDto requestDto = UserRequestDto.builder()
                .userId(mockUser.getUserId())
                .nickname("newHaru")
                .build();

        when(userRepository.findByUserIdQueryDSL(mockUser.getUserId())).thenReturn(Optional.of(mockUser));

        UserDto result = userService.completeRegistration(requestDto);

        assertNotNull(result);
        assertEquals(requestDto.getNickname(), result.getNickname());
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void testCompleteRegistration_UserNotFound() {
        UserRequestDto requestDto = UserRequestDto.builder()
                .userId(2L)
                .nickname("newHaru")
                .build();

        when(userRepository.findByUserIdQueryDSL(requestDto.getUserId())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> userService.completeRegistration(requestDto));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testUpdateUser_Success() {
        UserRequestDto updateDto = UserRequestDto.builder()
                .nickname("updatedHaru")
                .build();

        when(userRepository.findByUserIdQueryDSL(mockUser.getUserId())).thenReturn(Optional.of(mockUser));
        when(userRepository.existsByNicknameQueryDSL(updateDto.getNickname())).thenReturn(false);

        UserDto updatedUser = userService.updateUser(mockUser.getUserId(), updateDto);

        assertNotNull(updatedUser);
        assertEquals("updatedHaru", updatedUser.getNickname());

        verify(userRepository, times(1)).findByUserIdQueryDSL(mockUser.getUserId());
        verify(userRepository, times(1)).existsByNicknameQueryDSL(updateDto.getNickname());
    }

    @Test
    void testUpdateUser_DuplicateNickname() {
        UserRequestDto updateDto = UserRequestDto.builder()
                .nickname("dupHaru")
                .build();

        when(userRepository.findByUserIdQueryDSL(mockUser.getUserId())).thenReturn(Optional.of(mockUser));
        when(userRepository.existsByNicknameQueryDSL(updateDto.getNickname())).thenReturn(true);

        CustomException exception = assertThrows(CustomException.class, () -> userService.updateUser(mockUser.getUserId(), updateDto));
        assertEquals(ErrorCode.HAS_NICKNAME, exception.getErrorCode());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsByUserIdQueryDSL(mockUser.getUserId())).thenReturn(true);

        userService.deleteUser(mockUser.getUserId());

        verify(userRepository, times(1)).deleteByUserIdQueryDSL(mockUser.getUserId());
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.existsByUserIdQueryDSL(mockUser.getUserId())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class, () -> userService.deleteUser(mockUser.getUserId()));
        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testGetUserList_Success() {
        List<User> users = Arrays.asList(mockUser);
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> result = userService.getUserList();

        assertEquals(1, result.size());
        assertEquals(mockUser.getUserId(), result.get(0).getUserId());
        verify(userRepository, times(1)).findAll();
    }
}
