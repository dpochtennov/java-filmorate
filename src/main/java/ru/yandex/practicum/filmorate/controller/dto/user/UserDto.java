package ru.yandex.practicum.filmorate.controller.dto.user;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

@Data
public class UserDto {
    public static UserDto fromUser(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getFriends()
        );
    }

    private final Long id;
    private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;
    private final Set<Long> friends;
}
