package ru.yandex.practicum.filmorate.controller.dto.film;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.afterdate.AfterDate;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class CreateFilmDto {
    @NotBlank
    private final String name;
    @NotBlank
    @Size(max = 200)
    private final String description;
    @AfterDate("1895-12-27")
    private final LocalDate releaseDate;
    @Positive
    private final Long duration;

    public Film toFilm() {
        Film film = new Film();
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
        film.setDuration(Duration.ofMinutes(duration));
        return film;
    }
}
