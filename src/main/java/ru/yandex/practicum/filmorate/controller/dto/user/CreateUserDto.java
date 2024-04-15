package ru.yandex.practicum.filmorate.controller.dto.user;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Data
public class CreateUserDto {
    @NotEmpty
    @Email
    private final String email;
    @NotNull
    @Pattern(regexp = "^\\S+$")
    private final String login;
    private final String name;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

    public User toUser() {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        if (name != null && !name.isEmpty()) {
            user.setName(name);
        } else {
            user.setName(login);
        }
        user.setBirthday(birthday);
        return user;
    }
}