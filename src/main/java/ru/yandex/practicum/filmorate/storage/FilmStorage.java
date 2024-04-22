package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Film create(Film film);

    Optional<Film> findBy(Long id);

    Collection<Film> findAll();

    Film update(Film film);

    Collection<Film> getMostPopularFilms(Integer limit);
}
