package com.fortune.app.user.service.integration;

import com.fortune.app.FortuneAppBackendApplication;
import com.fortune.app.config.RedisTestConfig;
import com.fortune.app.user.dto.UserDto;
import com.fortune.app.user.dto.UserRequestDto;
import com.fortune.app.user.entity.User;
import com.fortune.app.user.repository.UserRepository;
import com.fortune.app.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

import java.sql.Date;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FortuneAppBackendApplication.class)
@EnableCaching
@ActiveProfiles("test")
@Import(RedisTestConfig.class)
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        redisTemplate.getConnectionFactory().getConnection().flushAll();

        savedUser = userRepository.save(
                User.builder()
                        .name("John Doe")
                        .birth(new Date(2000, 1, 1))
                        .email("john.doe@example.com")
                        .nickname("johnny")
                        .provider("Google")
                        .providerUid("1234")
                        .accessToken("access_token")
                        .refreshToken("refresh_token")
                        .build()
        );
    }

    // 회원 가입 테스트
    @Test
    void testSignUp_CachePut() {
        UserRequestDto requestDto = UserRequestDto.builder()
                .name("Jane Doe")
                .birth(new Date(2000, 1, 1))
                .email("jane.doe@example.com")
                .nickname("jane")
                .provider("Google")
                .providerUid("5678")
                .accessToken("access_token")
                .refreshToken("refresh_token")
                .build();

        UserDto userDto = userService.signUp(requestDto);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getEmail()).isEqualTo("jane.doe@example.com");

        Object cachedUser = redisTemplate.opsForValue().get("user::UserService_" + userDto.getUserId());
        assertThat(cachedUser).isNotNull();
    }

    // 회원 조회 테스트
    @Test
    void testGetUser_Cacheable() {
        UserDto userDto1 = userService.getUser(savedUser.getUserId());
        assertThat(userDto1).isNotNull();

        Object cachedUser = redisTemplate.opsForValue().get("user::UserService_" + savedUser.getUserId());
        assertThat(cachedUser).isNotNull();

        UserDto userDto2 = userService.getUser(savedUser.getUserId());
        assertThat(userDto2.getUserId()).isEqualTo(userDto1.getUserId());
        assertThat(userDto2.getEmail()).isEqualTo(userDto1.getEmail());
    }

    // 회원 수정 테스트
    @Test
    void testUpdateUser_CachePut() {
        UserRequestDto updateDto = UserRequestDto.builder()
                .name("John Updated")
                .build();

        UserDto updatedUser = userService.updateUser(savedUser.getUserId(), updateDto);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("John Updated");

        Object cachedUser = redisTemplate.opsForValue().get("user::UserService_" + savedUser.getUserId());
        assertThat(cachedUser).isNotNull();
    }

    // 회원 탈퇴 테스트
    @Test
    void testDeleteUser_CacheEvict() {
        userService.deleteUser(savedUser.getUserId());

        Optional<User> deletedUser = userRepository.findById(savedUser.getUserId());
        assertThat(deletedUser).isEmpty();

        Object cachedUser = redisTemplate.opsForValue().get("user::UserService_" + savedUser.getUserId());
        assertThat(cachedUser).isNull();
    }
}
