package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.dto.film.CreateFilmDto;
import ru.yandex.practicum.filmorate.controller.dto.film.FilmDto;
import ru.yandex.practicum.filmorate.controller.dto.film.UpdateFilmDto;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class FilmsController {
    private final FilmRepository filmRepository;
    private final IdGenerator idGenerator;
    private final Logger log = LoggerFactory.getLogger(FilmsController.class);

    public FilmsController(FilmRepository filmRepository, IdGenerator idGenerator) {
        this.filmRepository = filmRepository;
        this.idGenerator = idGenerator;
    }

    @PostMapping
    public ResponseEntity<FilmDto> createFilm(@Valid @RequestBody CreateFilmDto createFilmDto) {
        log.info("Create film request: {}", createFilmDto);
        Film film = createFilmDto.toFilm();
        Long id = idGenerator.generateId();
        film.setId(id);
        filmRepository.create(film);
        log.info("Film created: {}", film);
        return ResponseEntity.status(HttpStatus.CREATED).body(FilmDto.fromFilm(film));
    }

    @GetMapping
    public ResponseEntity<List<FilmDto>> getAllFilms() {
        log.info("Get all films request");
        List<FilmDto> films = filmRepository.findAll().stream().map(FilmDto::fromFilm).toList();
        log.info("Films found: {}", films);
        return ResponseEntity.ok(films);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FilmDto> getFilmById(@PathVariable Long id) {
        log.info("Get film by id request: {}", id);
        Optional<Film> maybeFilm = filmRepository.findBy(id);
        if (maybeFilm.isPresent()) {
            log.info("Film found: {}", maybeFilm.get());
            return ResponseEntity.ok(FilmDto.fromFilm(maybeFilm.get()));
        }
        log.info("Film not found: {}", id);
        return ResponseEntity.notFound().build();
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@Valid @RequestBody UpdateFilmDto updateFilmDto) {
        log.info("Update film request: {}", updateFilmDto);
        Optional<Film> maybeFilm = filmRepository.findBy(updateFilmDto.getId());
        if (maybeFilm.isEmpty()) {
            log.info("Film not found: {}", updateFilmDto.getId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updateFilmDto);
        }
        Film film = maybeFilm.get();
        updateFilmDto.updateFilm(film);
        filmRepository.update(film);
        log.info("Film updated: {}", film);
        return ResponseEntity.ok(FilmDto.fromFilm(film));
    }
}
