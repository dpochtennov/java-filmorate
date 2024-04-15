package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {

    public Long create(Film film);

    public Optional<Film> findBy(Long id);

    public Collection<Film> findAll();

    public Long update(Film film);
}
