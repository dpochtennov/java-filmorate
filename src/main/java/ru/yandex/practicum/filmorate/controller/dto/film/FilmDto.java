package ru.yandex.practicum.filmorate.controller.dto.film;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Data
public class FilmDto {
    public static FilmDto fromFilm(Film film) {
        return new FilmDto(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes()
        );
    }

    private final Long id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final Long duration;
}
