package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yandex.practicum.filmorate.exception.customExceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FilmServiceTest {

    private FilmStorage filmStorage;
    private UserStorage userStorage;

    private Film film;
    private User user;

    @BeforeEach
    void setUp() {
        film = new Film(
                1L,
                "Star Wars",
                "Science Fiction",
                LocalDate.of(1999, Month.AUGUST, 19),
                Duration.ofMinutes(90)
        );
        user = new User(
                1L,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1990, Month.APRIL, 1)
        );
        filmStorage = Mockito.mock(FilmStorage.class);
        userStorage = Mockito.mock(UserStorage.class);
    }

    @Test
    void create() {
        when(filmStorage.create(film)).thenReturn(film);

        FilmService filmService = new FilmService(filmStorage, userStorage);

        Film result = filmService.create(film);

        verify(filmStorage).create(film);
        assertEquals(film, result);
    }

    @Test
    void update() {
        when(filmStorage.findBy(film.getId())).thenReturn(Optional.of(film));
        when(filmStorage.update(film)).thenReturn(film);

        FilmService filmService = new FilmService(filmStorage, userStorage);

        Film result = filmService.update(film);

        verify(filmStorage).findBy(film.getId());
        verify(filmStorage).update(film);
        assertEquals(film, result);
    }

    @Test
    void updateNonExistentFilm() {
        when(filmStorage.findBy(film.getId())).thenReturn(Optional.empty());

        FilmService filmService = new FilmService(filmStorage, userStorage);
        assertThrows(EntityNotFoundException.class, () -> filmService.update(film));
    }

    @Test
    void updateDoNotRewritesLikedBy() {
        HashSet<Long> likedBy = new HashSet<>();
        likedBy.add(2L);
        film.setLikedBy(likedBy);
        Film updateFilm = new Film(
                1L,
                "Some film",
                "Some description",
                LocalDate.of(1999, Month.AUGUST, 19),
                Duration.ofMinutes(90)
        );
        when(filmStorage.findBy(updateFilm.getId())).thenReturn(Optional.of(film));

        FilmService filmService = new FilmService(filmStorage, userStorage);
        filmService.update(updateFilm);

        verify(filmStorage).findBy(updateFilm.getId());
        verify(filmStorage).update(updateFilm);
        assertEquals(updateFilm.getLikedBy(), likedBy);
    }

    @Test
    void findById() {
        Long id = 1L;
        when(filmStorage.findBy(id)).thenReturn(Optional.of(film));

        FilmService filmService = new FilmService(filmStorage, userStorage);

        Film result = filmService.findById(id);

        verify(filmStorage).findBy(id);
        assertEquals(film, result);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1L;
        when(filmStorage.findBy(id)).thenReturn(Optional.empty());

        FilmService filmService = new FilmService(filmStorage, userStorage);
        assertThrows(EntityNotFoundException.class, () -> filmService.findById(id));
    }

    @Test
    void findAll() {
        when(filmStorage.findAll()).thenReturn(List.of(film));

        FilmService filmService = new FilmService(filmStorage, userStorage);
        Collection<Film> result = filmService.findAll();
        Collection<Film> expected = List.of(film);

        verify(filmStorage).findAll();
        assertEquals(expected, result);
    }

    @Test
    void addLike() {
        Long id = 1L;
        when(filmStorage.findBy(id)).thenReturn(Optional.of(film));
        when(userStorage.findBy(id)).thenReturn(Optional.of(user));
        when(filmStorage.update(film)).thenReturn(film);

        FilmService filmService = new FilmService(filmStorage, userStorage);
        Film result = filmService.addLike(id, id);
        Set<Long> expected = Set.of(1L);

        verify(filmStorage).update(film);
        assertEquals(expected, result.getLikedBy());
    }

    @Test
    void addLikeWithNoFilm() {
        Long id = 1L;
        when(filmStorage.findBy(id)).thenReturn(Optional.empty());
        when(userStorage.findBy(id)).thenReturn(Optional.of(user));

        FilmService filmService = new FilmService(filmStorage, userStorage);
        assertThrows(EntityNotFoundException.class, () -> filmService.addLike(id, id));
    }

    @Test
    void addLikeWithNoUser() {
        Long id = 1L;
        when(filmStorage.findBy(id)).thenReturn(Optional.of(film));
        when(userStorage.findBy(id)).thenReturn(Optional.empty());

        FilmService filmService = new FilmService(filmStorage, userStorage);
        assertThrows(EntityNotFoundException.class, () -> filmService.addLike(id, id));
    }

    @Test
    void removeLike() {
        Long id = 1L;
        film.addLikedBy(1L);
        when(filmStorage.findBy(id)).thenReturn(Optional.of(film));
        when(userStorage.findBy(id)).thenReturn(Optional.of(user));
        when(filmStorage.update(film)).thenReturn(film);

        FilmService filmService = new FilmService(filmStorage, userStorage);
        Film result = filmService.removeLike(id, id);

        verify(filmStorage).update(film);
        assertEquals(0, result.getLikedBy().size());
    }

    @Test
    void removeLikeWithNoFilm() {
        Long id = 1L;
        when(filmStorage.findBy(id)).thenReturn(Optional.empty());
        when(userStorage.findBy(id)).thenReturn(Optional.of(user));

        FilmService filmService = new FilmService(filmStorage, userStorage);
        assertThrows(EntityNotFoundException.class, () -> filmService.removeLike(id, id));
    }

    @Test
    void removeLikeWithNoUser() {
        Long id = 1L;
        when(filmStorage.findBy(id)).thenReturn(Optional.of(film));
        when(userStorage.findBy(id)).thenReturn(Optional.empty());

        FilmService filmService = new FilmService(filmStorage, userStorage);
        assertThrows(EntityNotFoundException.class, () -> filmService.removeLike(id, id));
    }

    @Test
    void getMostPopularFilms() {
        when(filmStorage.getMostPopularFilms(any())).thenReturn(List.of(film));

        FilmService filmService = new FilmService(filmStorage, userStorage);
        Collection<Film> result = filmService.getMostPopularFilms(10);

        verify(filmStorage).getMostPopularFilms(10);
        assertEquals(List.of(film), result.stream().toList());
    }
}