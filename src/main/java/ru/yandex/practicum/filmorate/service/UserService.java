package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.customExceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User userToUpdate) {
        Optional<User> maybeUser = userStorage.findBy(userToUpdate.getId());
        if (maybeUser.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id '%s' not found", userToUpdate.getId()));
        }
        HashSet<Long> userFriends = maybeUser.get().getFriends();
        userToUpdate.setFriends(userFriends);
        return userStorage.update(userToUpdate);
    }

    public User findById(Long id) {
        Optional<User> maybeUser = userStorage.findBy(id);
        if (maybeUser.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id '%s' not found", id));
        }
        return maybeUser.get();
    }

    public Set<Long> getUserFriends(Long id) {
        Optional<User> maybeUser = userStorage.findBy(id);
        if (maybeUser.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id '%s' not found", id));
        }
        return maybeUser.get().getFriends();
    }

    public Set<Long> getCommonFriends(Long id, Long otherUserId) {
        Optional<User> maybeUser = userStorage.findBy(id);
        Optional<User> maybeOtherUser = userStorage.findBy(otherUserId);
        if (maybeUser.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id '%s' not found", id));
        }
        if (maybeOtherUser.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id '%s' not found", otherUserId));
        }
        HashSet<Long> resultingIntersection = new HashSet<>(maybeUser.get().getFriends());
        resultingIntersection.retainAll(maybeOtherUser.get().getFriends());
        return resultingIntersection;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User addToFriends(Long id, Long friendId) {
        Optional<User> maybeUser = userStorage.findBy(id);
        Optional<User> maybeFriend = userStorage.findBy(friendId);
        if (maybeUser.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id '%s' not found", id));
        }
        if (maybeFriend.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id '%s' not found", friendId));
        }
        User user = maybeUser.get();
        User friend = maybeFriend.get();
        user.addToFriend(friend.getId());
        friend.addToFriend(user.getId());
        userStorage.update(user);
        userStorage.update(friend);
        return user;
    }

    public User removeFromFriends(Long id, Long friendId) {
        Optional<User> maybeUser = userStorage.findBy(id);
        Optional<User> maybeFriend = userStorage.findBy(friendId);
        if (maybeUser.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id '%s' not found", id));
        }
        if (maybeFriend.isEmpty()) {
            throw new EntityNotFoundException(String.format("User with id '%s' not found", friendId));
        }
        User user = maybeUser.get();
        User friend = maybeFriend.get();
        user.removeFromFriends(friend.getId());
        friend.removeFromFriends(user.getId());
        userStorage.update(user);
        userStorage.update(friend);
        return user;
    }
}
