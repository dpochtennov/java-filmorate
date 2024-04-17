package ru.yandex.practicum.filmorate.util;

public class IdGenerator {
    private static long filmsIdCounter = 1L;
    private static long usersIdCounter = 1L;

    public static synchronized long generateFilmId() {
        return filmsIdCounter++;
    }

    public static synchronized long generateUserId() {
        return filmsIdCounter++;
    }
}
