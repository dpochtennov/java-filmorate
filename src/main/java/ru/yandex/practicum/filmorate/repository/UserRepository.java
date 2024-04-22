package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    User create(User user);

    Optional<User> findBy(Long id);

    Collection<User> findAll();

    User update(User user);
}
