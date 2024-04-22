package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.controller.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.controller.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmsController {
    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<FilmDto> createFilm(@Valid @RequestBody CreateFilmDto createFilmDto) {
        Film film = filmService.create(createFilmDto.toFilm());
        return ResponseEntity.status(HttpStatus.CREATED).body(FilmDto.fromFilm(film));
    }

    @GetMapping
    public ResponseEntity<List<FilmDto>> getAllFilms() {
        List<FilmDto> films = filmService.findAll().stream().map(FilmDto::fromFilm).toList();
        return ResponseEntity.ok(films);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<FilmDto>> getMostPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        Collection<Film> popularFilms = filmService.getMostPopularFilms(count);
        return ResponseEntity.ok(popularFilms.stream().map(FilmDto::fromFilm).toList());
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<FilmDto> addLike(@PathVariable Long id, @PathVariable Long userId) {
        Film updatedFilm = filmService.addLike(id, userId);
        return ResponseEntity.ok(FilmDto.fromFilm(updatedFilm));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<FilmDto> removeLike(@PathVariable Long id, @PathVariable Long userId) {
        Film updatedFilm = filmService.removeLike(id, userId);
        return ResponseEntity.ok(FilmDto.fromFilm(updatedFilm));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> getFilmById(@PathVariable Long id) {
        Film film = filmService.findById(id);
        return ResponseEntity.ok(FilmDto.fromFilm(film));
    }

    @PutMapping
    public ResponseEntity<FilmDto> updateFilm(@Valid @RequestBody UpdateFilmDto updateFilmDto) {
        Film updatedFilm = filmService.update(updateFilmDto.toFilm());
        return ResponseEntity.ok(FilmDto.fromFilm(updatedFilm));
    }
}
