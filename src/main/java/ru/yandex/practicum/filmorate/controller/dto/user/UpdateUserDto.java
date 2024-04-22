package ru.yandex.practicum.filmorate.controller.dto.user;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Data
public class UpdateUserDto {
    @NotNull
    private Long id;
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
        user.setId(id);
        user.setEmail(email);
        user.setLogin(login);
        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }
        user.setBirthday(birthday);
        return user;
    }
}
