package com.fortune.app.user.service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fortune.app.FortuneAppBackendApplication;
import com.fortune.app.common.util.CacheNames;
import com.fortune.app.common.util.CacheUtil;
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
    @Autowired
    private ObjectMapper objectMapper;

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        redisTemplate.getConnectionFactory().getConnection().flushAll();

        savedUser = userRepository.save(
                User.builder()
                        .name("John Doe")
                        .email("john.doe@example.com")
                        .provider("Google")
                        .providerUid("1234")
                        .accessToken("access_token")
                        .refreshToken("refresh_token")
                        .isRegistered(true)
                        .build()
        );

        userRepository.flush();
    }

    @Test
    void testRedisConnection() {
        assertThat(redisTemplate.getConnectionFactory()).isNotNull();
    }

    // 회원 가입 테스트
    @Test
    void testSignUp_CachePut() {
        UserRequestDto requestDto = UserRequestDto.builder()
                .userId(savedUser.getUserId())
                .birth(new Date(2000, 1, 1))
                .nickname("john")
                .build();

        UserDto userDto = userService.completeRegistration(requestDto);

        assertThat(userDto).isNotNull();

        Object cachedUser = redisTemplate.opsForValue().get(CacheNames.USER + "::" + CacheUtil.getUserCacheKey(userDto.getUserId()));
        assertThat(cachedUser).isNotNull();
    }

    // 회원 조회 테스트
    @Test
    void testGetUser_Cacheable() throws Exception {
        Object beforeCache = redisTemplate.opsForValue().get(CacheNames.USER + "::" + CacheUtil.getUserCacheKey(savedUser.getUserId()));
        assertThat(beforeCache).isNull();

        UserDto userDto1 = userService.getUser(savedUser.getUserId());
        assertThat(userDto1).isNotNull();

        Object afterCache = redisTemplate.opsForValue().get(CacheNames.USER + "::" + CacheUtil.getUserCacheKey(savedUser.getUserId()));
        assertThat(afterCache).isNotNull();

        UserDto cachedUserDto = objectMapper.readValue(objectMapper.writeValueAsString(afterCache), UserDto.class);
        assertThat(cachedUserDto).isNotNull();

        assertThat(cachedUserDto.getUserId()).isEqualTo(userDto1.getUserId());
        assertThat(cachedUserDto.getEmail()).isEqualTo(userDto1.getEmail());
    }

    // 회원 수정 테스트
    @Test
    void testUpdateUser_CachePut() {
        UserRequestDto updateDto = UserRequestDto.builder()
                .nickname("John Updated")
                .build();

        UserDto updatedUser = userService.updateUser(savedUser.getUserId(), updateDto);

        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getUserId()).isEqualTo(savedUser.getUserId());
        assertThat(updatedUser.getNickname()).isEqualTo("John Updated");

        Object cachedUser = redisTemplate.opsForValue().get(CacheNames.USER + "::" + CacheUtil.getUserCacheKey(savedUser.getUserId()));
        assertThat(cachedUser).isNotNull();
    }

    // 회원 탈퇴 테스트
    @Test
    void testDeleteUser_CacheEvict() {
        userService.deleteUser(savedUser.getUserId());

        Optional<User> deletedUser = userRepository.findById(savedUser.getUserId());
        assertThat(deletedUser).isEmpty();

        Object cachedUser = redisTemplate.opsForValue().get(CacheNames.USER + "::" + CacheUtil.getUserCacheKey(savedUser.getUserId()));
        assertThat(cachedUser).isNull();
    }
}
