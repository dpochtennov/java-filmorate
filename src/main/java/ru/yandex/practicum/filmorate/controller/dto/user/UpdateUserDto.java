package ru.yandex.practicum.filmorate.controller.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Data
public class UpdateUserDto {
    @NotNull
    private Long id;
    @Email
    private String email;
    @Pattern(regexp = "^\\S+$")
    private String login;
    private String name;
    private LocalDate birthday;

    public User updateUser(User user) {
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }
        if (login != null) {
            user.setLogin(login);
        }
        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }
        if (birthday != null && birthday.isBefore(LocalDate.now())) {
            user.setBirthday(birthday);
        }
        return user;
    }
}
