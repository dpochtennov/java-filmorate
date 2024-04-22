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
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UsersController {
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        log.info("Create user request: {}", createUserDto);
        User user = userRepository.create(createUserDto.toUser());
        log.info("User created: {}", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDto.fromUser(user));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        log.info("Get all users request");
        List<UserDto> users = userRepository.findAll().stream().map(UserDto::fromUser).toList();
        log.info("Users found: {}", users);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.info("Get user by id request: {}", id);
        Optional<User> maybeUser = userRepository.findBy(id);
        if (maybeUser.isPresent()) {
            log.info("User found: {}", maybeUser.get());
            return ResponseEntity.ok(UserDto.fromUser(maybeUser.get()));
        }
        log.info("Film not found: {}", id);
        return ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserDto updateUserDto) {
        log.info("Update user request: {}", updateUserDto);
        Optional<User> maybeUser = userRepository.findBy(updateUserDto.getId());
        if (maybeUser.isEmpty()) {
            log.info("User not found: {}", updateUserDto.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updateUserDto);
        }
        User user = updateUserDto.toUser(maybeUser.get());
        User updatedUser = userRepository.update(user);
        log.info("User updated: {}", user);
        return ResponseEntity.ok(UserDto.fromUser(updatedUser));
    }
}
