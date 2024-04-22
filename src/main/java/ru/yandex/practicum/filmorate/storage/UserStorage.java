package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    User create(User user);

    Optional<User> findBy(Long id);

    Collection<User> findAll();

    User update(User user);
}
