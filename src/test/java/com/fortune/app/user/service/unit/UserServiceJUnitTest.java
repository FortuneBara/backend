package com.fortune.app.user.service.unit;

import com.fortune.app.FortuneAppBackendApplication;
import com.fortune.app.user.dto.UserDto;
import com.fortune.app.user.dto.UserRequestDto;
import com.fortune.app.user.entity.User;
import com.fortune.app.user.repository.UserRepository;
import com.fortune.app.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FortuneAppBackendApplication.class)
@Transactional
class UserServiceJUnitTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        redisTemplate.getConnectionFactory().getConnection().flushAll();

        savedUser = userRepository.save(
                User.builder()
                        .name("Haru")
                        .email("haru.every@example.com")
                        .provider("Google")
                        .providerUid("1234")
                        .isRegistered(true)
                        .build()
        );

        userRepository.flush();
    }

    @AfterEach
    void clearCacheAfterTest() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void testGetUser_Success() {
        UserDto userDto = userService.getUser(savedUser.getUserId());

        assertThat(userDto)
                .usingRecursiveComparison()
                .ignoringFields("isRegistered", "createdAt", "updatedAt")
                .isEqualTo(savedUser);
    }

    @Test
    void testCompleteRegistration_Success() {
        UserRequestDto requestDto = UserRequestDto.builder()
                .userId(savedUser.getUserId())
                .nickname("haruharu")
                .build();

        UserDto userDto = userService.completeRegistration(requestDto);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getNickname()).isEqualTo("haruharu");
    }

    @Test
    void testDeleteUser_Success() {
        userService.deleteUser(savedUser.getUserId());

        Optional<User> deletedUser = userRepository.findByUserIdQueryDSL(savedUser.getUserId());
        assertThat(deletedUser).isEmpty();
    }
}
