package ru.yandex.practicum.filmorate.infrastructure;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;

@Component
public class InMemoryUserRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Long create(User user) {
        users.put(user.getId(), user);
        return user.getId();
    }

    @Override
    public Optional<User> findBy(Long id) {
        User user = users.getOrDefault(id, null);
        return Optional.ofNullable(user);
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Long update(User user) {
        users.put(user.getId(), user);
        return user.getId();
    }
}
