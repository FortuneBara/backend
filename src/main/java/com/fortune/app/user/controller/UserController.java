package com.fortune.app.user.controller;

import com.fortune.app.common.aop.MethodTimeCheck;
import com.fortune.app.user.dto.UserDto;
import com.fortune.app.user.dto.UserRequestDto;
import com.fortune.app.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.fortune.app.user.validation.UserValidationGroups.SignUpGroup;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @MethodTimeCheck
    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> completeSignup(@Validated(SignUpGroup.class) @RequestBody UserRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.completeRegistration(dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userId));
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @Valid @RequestBody UserRequestDto dto) {
        UserDto userDto = userService.updateUser(userId, dto);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUserList() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserList());
    }
}
