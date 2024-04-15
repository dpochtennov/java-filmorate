package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    public Long create(User user);

    public Optional<User> findBy(Long id);

    public Collection<User> findAll();

    public Long update(User user);
}
