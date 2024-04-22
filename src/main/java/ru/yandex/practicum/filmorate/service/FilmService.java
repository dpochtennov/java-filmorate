package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.customExceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film filmToUpdate) {
        Optional<Film> maybeFilm = filmStorage.findBy(filmToUpdate.getId());
        if (maybeFilm.isEmpty()) {
            throw new EntityNotFoundException(String.format("Film with id '%s' not found", filmToUpdate.getId()));
        }
        HashSet<Long> likedBy = maybeFilm.get().getLikedBy();
        filmToUpdate.setLikedBy(likedBy);
        return filmStorage.update(filmToUpdate);
    }

    public Film findById(Long id) {
        Optional<Film> maybeFilm = filmStorage.findBy(id);
        if (maybeFilm.isEmpty()) {
            throw new EntityNotFoundException(String.format("Film with id '%s' not found", id));
        }
        return maybeFilm.get();
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film addLike(Long filmId, Long userId) {
        Optional<Film> maybeFilm = filmStorage.findBy(filmId);
        Optional<User> maybeUser = userStorage.findBy(userId);
        if (maybeFilm.isEmpty()) {
            throw new EntityNotFoundException(String.format("Film with id '%s' not found", filmId));
        }
        if (maybeUser.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id '%s' not found", userId));
        }
        Film film = maybeFilm.get();
        film.addLikedBy(userId);
        return filmStorage.update(film);
    }

    public Film removeLike(Long filmId, Long userId) {
        Optional<Film> maybeFilm = filmStorage.findBy(filmId);
        Optional<User> maybeUser = userStorage.findBy(userId);
        if (maybeFilm.isEmpty()) {
            throw new EntityNotFoundException(String.format("Film with id '%s' not found", filmId));
        }
        if (maybeUser.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id '%s' not found", userId));
        }
        Film film = maybeFilm.get();
        film.removeLikedBy(userId);
        return filmStorage.update(film);
    }

    public Collection<Film> getMostPopularFilms(Integer limit) {
        return filmStorage.getMostPopularFilms(limit);
    }
}
