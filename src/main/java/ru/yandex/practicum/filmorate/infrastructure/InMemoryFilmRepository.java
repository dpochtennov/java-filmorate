package ru.yandex.practicum.filmorate.infrastructure;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.*;

@Component
public class InMemoryFilmRepository implements FilmRepository {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Long create(Film film) {
        films.put(film.getId(), film);
        return film.getId();
    }

    @Override
    public Optional<Film> findBy(Long id) {
        Film film = films.getOrDefault(id, null);
        return Optional.ofNullable(film);
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Long update(Film film) {
        films.put(film.getId(), film);
        return film.getId();
    }
}
