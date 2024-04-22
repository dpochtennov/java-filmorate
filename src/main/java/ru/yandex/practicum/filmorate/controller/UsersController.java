package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.dto.user.CreateUserDto;
import ru.yandex.practicum.filmorate.controller.dto.user.UpdateUserDto;
import ru.yandex.practicum.filmorate.controller.dto.user.UserDto;
import ru.yandex.practicum.filmorate.controller.dto.user.UserIdDto;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        User user = userService.create(createUserDto.toUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.fromUser(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.findAll().stream().map(UserDto::fromUser).toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/friends/common/{otherUserId}")
    public ResponseEntity<List<UserIdDto>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherUserId) {
        Set<Long> commonFriends = userService.getCommonFriends(id, otherUserId);
        return ResponseEntity.ok(commonFriends.stream().map(UserIdDto::new).toList());
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserDto> addToFriends(@PathVariable Long id, @PathVariable Long friendId) {
        User updatedUser = userService.addToFriends(id, friendId);
        return ResponseEntity.ok(UserDto.fromUser(updatedUser));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserDto> removeFromFriends(@PathVariable Long id, @PathVariable Long friendId) {
        User updatedUser = userService.removeFromFriends(id, friendId);
        return ResponseEntity.ok(UserDto.fromUser(updatedUser));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<UserIdDto>> getUserFriends(@PathVariable Long id) {
        Set<Long> userFriends = userService.getUserFriends(id);
        return ResponseEntity.ok(userFriends.stream().map(UserIdDto::new).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        User maybeUser = userService.findById(id);
        return ResponseEntity.ok(UserDto.fromUser(maybeUser));
    }

    @PutMapping
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto) {
        User updatedUser = userService.update(updateUserDto.toUser());
        return ResponseEntity.ok(UserDto.fromUser(updatedUser));
    }
}
