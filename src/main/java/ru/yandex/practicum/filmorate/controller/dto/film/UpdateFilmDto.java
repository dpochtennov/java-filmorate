package ru.yandex.practicum.filmorate.controller.dto.film;

import jakarta.validation.constraints.*;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;

@Data
public class UpdateFilmDto {
    @NotNull
    private final Long id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final Long duration;

    public Film updateFilm(Film film) {
        if (name != null && !name.isEmpty()) {
            film.setName(name);
        }
        if (description != null && !description.isEmpty() && description.length() <= 200) {
            film.setDescription(description);
        }
        if (releaseDate != null && releaseDate.isAfter(LocalDate.of(1895, Month.DECEMBER, 27))) {
            film.setReleaseDate(releaseDate);
        }
        if (duration != null && duration > 0) {
            film.setDuration(Duration.ofMinutes(duration));
        }
        return film;
    }
}
