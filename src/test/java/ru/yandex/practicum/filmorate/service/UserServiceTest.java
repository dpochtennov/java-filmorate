package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yandex.practicum.filmorate.exception.customExceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private UserStorage userStorage;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(
                1L,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        userStorage = Mockito.mock(UserStorage.class);
    }

    @Test
    void create() {
        when(userStorage.create(user)).thenReturn(user);

        UserService userService = new UserService(userStorage);

        User result = userService.create(user);

        verify(userStorage).create(user);
        assertEquals(user, result);
    }

    @Test
    void update() {
        when(userStorage.findBy(user.getId())).thenReturn(Optional.of(user));
        when(userStorage.update(user)).thenReturn(user);

        UserService userService = new UserService(userStorage);

        User result = userService.update(user);

        verify(userStorage).findBy(user.getId());
        verify(userStorage).update(user);
        assertEquals(user, result);
    }

    @Test
    void updateNonExistentFilm() {
        when(userStorage.findBy(user.getId())).thenReturn(Optional.empty());

        UserService userService = new UserService(userStorage);
        assertThrows(EntityNotFoundException.class, () -> userService.update(user));
    }

    @Test
    void updateDoNotRewritesFriends() {
        user.addToFriend(2L);
        User updateUser = new User(
                1L,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userStorage.findBy(updateUser.getId())).thenReturn(Optional.of(user));
        when(userStorage.update(updateUser)).thenReturn(updateUser);

        UserService userService = new UserService(userStorage);

        User result = userService.update(updateUser);

        verify(userStorage).findBy(updateUser.getId());
        verify(userStorage).update(updateUser);
        assertEquals(result.getFriends().size(), 1);
    }

    @Test
    void findById() {
        Long id = 1L;
        when(userStorage.findBy(id)).thenReturn(Optional.of(user));

        UserService userService = new UserService(userStorage);

        User result = userService.findById(id);

        verify(userStorage).findBy(id);
        assertEquals(user, result);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1L;
        when(userStorage.findBy(id)).thenReturn(Optional.empty());

        UserService userService = new UserService(userStorage);
        assertThrows(EntityNotFoundException.class, () -> userService.findById(id));
    }

    @Test
    void findAll() {
        when(userStorage.findAll()).thenReturn(List.of(user));

        UserService userService = new UserService(userStorage);
        Collection<User> result = userService.findAll();
        Collection<User> expected = List.of(user);

        verify(userStorage).findAll();
        assertEquals(expected, result);
    }

    @Test
    void addToFriends() {
        User secondUser = new User(
                2L,
                "email@test.com",
                "login_test",
                "John Wicked",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userStorage.findBy(user.getId())).thenReturn(Optional.of(user));
        when(userStorage.findBy(secondUser.getId())).thenReturn(Optional.of(secondUser));

        UserService userService = new UserService(userStorage);
        User result = userService.addToFriends(user.getId(), secondUser.getId());

        verify(userStorage).update(user);
        verify(userStorage).update(secondUser);

        assertEquals(secondUser.getFriends().size(), 1);
        assertTrue(secondUser.getFriends().contains(result.getId()));
        assertEquals(result.getFriends().size(), 1);
        assertTrue(result.getFriends().contains(secondUser.getId()));
    }

    @Test
    void addToFriendsNonExistentUser() {
        User secondUser = new User(
                2L,
                "email@test.com",
                "login_test",
                "John Wicked",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userStorage.findBy(user.getId())).thenReturn(Optional.empty());
        when(userStorage.findBy(secondUser.getId())).thenReturn(Optional.of(secondUser));

        UserService userService = new UserService(userStorage);
        assertThrows(EntityNotFoundException.class, () -> userService.addToFriends(user.getId(), secondUser.getId()));
    }

    @Test
    void addToFriendsNonExistentOtherUser() {
        User secondUser = new User(
                2L,
                "email@test.com",
                "login_test",
                "John Wicked",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userStorage.findBy(user.getId())).thenReturn(Optional.of(user));
        when(userStorage.findBy(secondUser.getId())).thenReturn(Optional.empty());

        UserService userService = new UserService(userStorage);
        assertThrows(EntityNotFoundException.class, () -> userService.addToFriends(user.getId(), secondUser.getId()));
    }

    @Test
    void removeFromFriends() {
        User secondUser = new User(
                2L,
                "email@test.com",
                "login_test",
                "John Wicked",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        user.addToFriend(secondUser.getId());
        secondUser.addToFriend(user.getId());
        when(userStorage.findBy(user.getId())).thenReturn(Optional.of(user));
        when(userStorage.findBy(secondUser.getId())).thenReturn(Optional.of(secondUser));

        UserService userService = new UserService(userStorage);
        User result = userService.removeFromFriends(user.getId(), secondUser.getId());

        verify(userStorage).update(user);
        verify(userStorage).update(secondUser);

        assertEquals(secondUser.getFriends().size(), 0);
        assertEquals(result.getFriends().size(), 0);
    }

    @Test
    void removeFromFriendsNonExistentUser() {
        User secondUser = new User(
                2L,
                "email@test.com",
                "login_test",
                "John Wicked",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userStorage.findBy(user.getId())).thenReturn(Optional.empty());
        when(userStorage.findBy(secondUser.getId())).thenReturn(Optional.of(secondUser));

        UserService userService = new UserService(userStorage);
        assertThrows(EntityNotFoundException.class,
                () -> userService.removeFromFriends(user.getId(), secondUser.getId()));
    }

    @Test
    void removeFromFriendsNonExistentOtherUser() {
        User secondUser = new User(
                2L,
                "email@test.com",
                "login_test",
                "John Wicked",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userStorage.findBy(user.getId())).thenReturn(Optional.of(user));
        when(userStorage.findBy(secondUser.getId())).thenReturn(Optional.empty());

        UserService userService = new UserService(userStorage);
        assertThrows(EntityNotFoundException.class,
                () -> userService.removeFromFriends(user.getId(), secondUser.getId()));
    }

    @Test
    void getUserFriends() {
        user.addToFriend(2L);
        when(userStorage.findBy(user.getId())).thenReturn(Optional.of(user));

        UserService userService = new UserService(userStorage);
        Set<Long> result = userService.getUserFriends(user.getId());

        assertEquals(result, Set.of(2L));
    }

    @Test
    void getUserFriendsNonExistentUser() {
        when(userStorage.findBy(user.getId())).thenReturn(Optional.empty());

        UserService userService = new UserService(userStorage);
        assertThrows(EntityNotFoundException.class, () -> userService.getUserFriends(user.getId()));
    }

    @Test
    void getCommonFriends() {
        User secondUser = new User(
                2L,
                "email@test.com",
                "login_test",
                "John Wicked",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        user.addToFriend(3L);
        user.addToFriend(4L);
        secondUser.addToFriend(3L);
        secondUser.addToFriend(5L);
        when(userStorage.findBy(user.getId())).thenReturn(Optional.of(user));
        when(userStorage.findBy(secondUser.getId())).thenReturn(Optional.of(secondUser));

        UserService userService = new UserService(userStorage);
        Set<Long> result = userService.getCommonFriends(user.getId(), secondUser.getId());

        assertEquals(result.size(), 1);
        assertTrue(result.contains(3L));
    }

    @Test
    void getCommonFriendsNonExistentUser() {
        User secondUser = new User(
                2L,
                "email@test.com",
                "login_test",
                "John Wicked",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userStorage.findBy(user.getId())).thenReturn(Optional.empty());
        when(userStorage.findBy(secondUser.getId())).thenReturn(Optional.of(secondUser));

        UserService userService = new UserService(userStorage);
        assertThrows(EntityNotFoundException.class,
                () -> userService.getCommonFriends(user.getId(), secondUser.getId()));
    }

    @Test
    void getCommonFriendsNonExistentOtherUser() {
        User secondUser = new User(
                2L,
                "email@test.com",
                "login_test",
                "John Wicked",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userStorage.findBy(user.getId())).thenReturn(Optional.of(user));
        when(userStorage.findBy(secondUser.getId())).thenReturn(Optional.empty());

        UserService userService = new UserService(userStorage);
        assertThrows(EntityNotFoundException.class,
                () -> userService.getCommonFriends(user.getId(), secondUser.getId()));
    }
}