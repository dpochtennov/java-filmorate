package ru.yandex.practicum.filmorate.util;

public class IdGenerator {
    private static long idCounter = 1L;

    public static synchronized long generateId() {
        return idCounter++;
    }
}
