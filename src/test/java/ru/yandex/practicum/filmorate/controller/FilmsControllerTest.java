package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(FilmsController.class)
public class FilmsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmRepository filmRepository;

    @MockBean
    private IdGenerator idGenerator;

    @Test
    public void testGetFilmById() throws Exception {
        Long id = 1L;
        Film film = new Film(
            id,
            "Star Wars",
            "Science Fiction",
            LocalDate.of(1999, Month.AUGUST, 19),
            Duration.ofMinutes(90)
        );
        when(filmRepository.findBy(id)).thenReturn(Optional.of(film));

        mockMvc.perform(MockMvcRequestBuilders.get("/films/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"name\":\"Star Wars\",\"description\":" +
                        "\"Science Fiction\",\"releaseDate\":\"1999-08-19\", \"duration\": 90 }"));

    }

    @Test
    public void testGetFilmByIdNotFound() throws Exception {
        Long id = 1L;
        when(filmRepository.findBy(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/films/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testGetFilms() throws Exception {
        Film film = new Film(
                1L,
                "Star Wars",
                "Science Fiction",
                LocalDate.of(1999, Month.AUGUST, 19),
                Duration.ofMinutes(90)
        );
        when(filmRepository.findAll()).thenReturn(List.of(film));

        mockMvc.perform(MockMvcRequestBuilders.get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\": 1, \"name\":\"Star Wars\",\"description\":" +
                        "\"Science Fiction\",\"releaseDate\":\"1999-08-19\", \"duration\": 90 }]"));

    }

    @Test
    public void testCreateFilmSuccessfully() throws Exception {
        when(idGenerator.generateId()).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Star Wars\",\"description\":" +
                                "\"Science Fiction\",\"releaseDate\":\"1999-08-19\", \"duration\": 90 }")
                )
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\": 1, \"name\":\"Star Wars\",\"description\":" +
                        "\"Science Fiction\",\"releaseDate\":\"1999-08-19\", \"duration\": 90 }"));

    }

    @Test
    public void testCreateFilmEmptyName() throws Exception {
        when(idGenerator.generateId()).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"description\":" +
                                "\"Science Fiction\",\"releaseDate\":\"1999-08-19\", \"duration\": 90 }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateFilmEmptyDescription() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Star Wars\",\"description\":" +
                                "\"\",\"releaseDate\":\"1999-08-19\", \"duration\": 90 }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateFilmTooLongDescription() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Star Wars\",\"description\":" +
                                "\"" + "abc".repeat(100) + "\",\"releaseDate\":\"1999-08-19\", \"duration\": 90 }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateFilmNullDescription() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Star Wars\",\"releaseDate\":\"1999-08-19\", \"duration\": 90 }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateFilmNullReleaseDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Star Wars\",\"description\":" +
                                "\"Science Fiction\", \"duration\": 90 }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateFilmReleaseDateTooInThePast() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Star Wars\",\"description\":" +
                                "\"Science Fiction\",\"releaseDate\":\"1799-08-19\", \"duration\": 90 }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateFilmNullDuration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Star Wars\",\"description\":" +
                                "\"Science Fiction\",\"releaseDate\":\"1799-08-19\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateFilmNegativeDuration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Star Wars\",\"description\":" +
                                "\"Science Fiction\",\"releaseDate\":\"1799-08-19\", \"duration\": -5 }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateFilmZero() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Star Wars\",\"description\":" +
                                "\"Science Fiction\",\"releaseDate\":\"1799-08-19\", \"duration\": 90 }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateFilmById() throws Exception {
        Long id = 1L;
        Film film = new Film(
                id,
                "Star Wars",
                "Science Fiction",
                LocalDate.of(1999, Month.AUGUST, 19),
                Duration.ofMinutes(90)
        );
        when(filmRepository.findBy(id)).thenReturn(Optional.of(film));

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1, \"name\":\"Some film\",\"description\":" +
                        "\"Some description\",\"releaseDate\":\"2009-08-19\", \"duration\": 50 }")
            )
            .andExpect(status().isOk())
            .andExpect(content().json("{\"id\": 1, \"name\":\"Some film\",\"description\":" +
                    "\"Some description\",\"releaseDate\":\"2009-08-19\", \"duration\": 50 }"));

    }

    @Test
    public void testUpdateFilmByIdNoId() throws Exception {
        Long id = 1L;
        Film film = new Film(
                id,
                "Star Wars",
                "Science Fiction",
                LocalDate.of(1999, Month.AUGUST, 19),
                Duration.ofMinutes(90)
        );
        when(filmRepository.findBy(id)).thenReturn(Optional.of(film));

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Some film\",\"description\":" +
                                "\"Some description\",\"releaseDate\":\"2009-08-19\", \"duration\": 50 }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateFilmByIdEmptyName() throws Exception {
        Long id = 1L;
        Film film = new Film(
                id,
                "Star Wars",
                "Science Fiction",
                LocalDate.of(1999, Month.AUGUST, 19),
                Duration.ofMinutes(90)
        );
        when(filmRepository.findBy(id)).thenReturn(Optional.of(film));

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\":\"\",\"description\":" +
                                "\"Some description\",\"releaseDate\":\"2009-08-19\", \"duration\": 50 }")
                )
                .andExpect(content().json("{\"id\": 1, \"name\":\"Star Wars\",\"description\":" +
                        "\"Some description\",\"releaseDate\":\"2009-08-19\", \"duration\": 50 }"));

    }

    @Test
    public void testUpdateFilmByIdEmptyDescription() throws Exception {
        Long id = 1L;
        Film film = new Film(
                id,
                "Star Wars",
                "Science Fiction",
                LocalDate.of(1999, Month.AUGUST, 19),
                Duration.ofMinutes(90)
        );
        when(filmRepository.findBy(id)).thenReturn(Optional.of(film));

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\":\"\",\"description\":" +
                                "\"\",\"releaseDate\":\"2009-08-19\", \"duration\": 50 }")
                )
                .andExpect(content().json("{\"id\": 1, \"name\":\"Star Wars\",\"description\":" +
                        "\"Science Fiction\",\"releaseDate\":\"2009-08-19\", \"duration\": 50 }"));

    }

    @Test
    public void testUpdateFilmByIdTooLongDescription() throws Exception {
        Long id = 1L;
        Film film = new Film(
                id,
                "Star Wars",
                "Science Fiction",
                LocalDate.of(1999, Month.AUGUST, 19),
                Duration.ofMinutes(90)
        );
        when(filmRepository.findBy(id)).thenReturn(Optional.of(film));

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\":\"Some name\",\"description\":\"" + "abc".repeat(100) +
                                "\", \"releaseDate\":\"2009-08-19\", \"duration\": 50 }")
                )
                .andExpect(content().json("{\"id\": 1, \"name\":\"Some name\",\"description\":" +
                        "\"Science Fiction\",\"releaseDate\":\"2009-08-19\", \"duration\": 50 }"));

    }

    @Test
    public void testUpdateFilmByIdTooOldReleaseDate() throws Exception {
        Long id = 1L;
        Film film = new Film(
                id,
                "Star Wars",
                "Science Fiction",
                LocalDate.of(1999, Month.AUGUST, 19),
                Duration.ofMinutes(90)
        );
        when(filmRepository.findBy(id)).thenReturn(Optional.of(film));

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\":\"Some name\",\"description\":" +
                                "\"Some description\",\"releaseDate\":\"1788-08-19\", \"duration\": 50 }")
                )
                .andExpect(content().json("{\"id\": 1, \"name\":\"Some name\",\"description\":" +
                        "\"Some description\",\"releaseDate\":\"1999-08-19\", \"duration\": 50 }"));

    }

    @Test
    public void testUpdateFilmByIdDurationIsNegative() throws Exception {
        Long id = 1L;
        Film film = new Film(
                id,
                "Star Wars",
                "Science Fiction",
                LocalDate.of(1999, Month.AUGUST, 19),
                Duration.ofMinutes(90)
        );
        when(filmRepository.findBy(id)).thenReturn(Optional.of(film));

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\":\"Some name\",\"description\":" +
                                "\"Some description\",\"releaseDate\":\"1999-08-19\", \"duration\": -50 }")
                )
                .andExpect(content().json("{\"id\": 1, \"name\":\"Some name\",\"description\":" +
                        "\"Some description\",\"releaseDate\":\"1999-08-19\", \"duration\": 90 }"));

    }

    @Test
    public void testUpdateFilmByIdDurationIsZero() throws Exception {
        Long id = 1L;
        Film film = new Film(
                id,
                "Star Wars",
                "Science Fiction",
                LocalDate.of(1999, Month.AUGUST, 19),
                Duration.ofMinutes(90)
        );
        when(filmRepository.findBy(id)).thenReturn(Optional.of(film));

        mockMvc.perform(MockMvcRequestBuilders.put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"name\":\"Some name\",\"description\":" +
                                "\"Some description\",\"releaseDate\":\"1999-08-19\", \"duration\": 0 }")
                )
                .andExpect(content().json("{\"id\": 1, \"name\":\"Some name\",\"description\":" +
                        "\"Some description\",\"releaseDate\":\"1999-08-19\", \"duration\": 90 }"));

    }
}