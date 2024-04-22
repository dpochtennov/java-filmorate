package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        Long id = IdGenerator.generateFilmId();
        film.setId(id);
        films.put(id, film);
        return film;
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
    public Film update(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getMostPopularFilms(Integer limit) {
        return films.values().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikedBy().size())).limit(limit)
                .toList().reversed();
    }
}
