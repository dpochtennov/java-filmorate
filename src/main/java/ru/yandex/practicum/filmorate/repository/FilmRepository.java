package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {

    Film create(Film film);

    Optional<Film> findBy(Long id);

    Collection<Film> findAll();

    Film update(Film film);
}
