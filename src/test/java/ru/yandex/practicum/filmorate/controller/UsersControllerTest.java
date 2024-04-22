package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.exception.customExceptions.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testGetUserById() throws Exception {
        Long id = 1L;
        User user = new User(
                id,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userService.findById(id)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"email@test.com\",\"login\":" +
                        "\"login_test\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\", \"friends\":[] }"));

    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        Long id = 1L;
        when(userService.findById(id)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    public void testGetUsers() throws Exception {
        User user = new User(
                1L,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\": 1, \"email\":\"email@test.com\",\"login\":" +
                        "\"login_test\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }]"));

    }

    @Test
    public void testCreateUserSuccessfully() throws Exception {
        User expectedUser = new User(
                1L,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userService.create(any())).thenReturn(expectedUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email@test.com\",\"login\":" +
                                "\"login_test\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\": 1, \"email\":\"email@test.com\",\"login\":" +
                        "\"login_test\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }"));

    }

    @Test
    public void testCreateUserEmptyEmail() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\",\"login\":" +
                                "\"login_test\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateUserInvalidEmailFormat() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"@email@email.ru\",\"login\":" +
                                "\"login_test\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateUserNullEmail() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":" +
                                "\"login_test\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateUserEmptyLogin() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"some@email.com\",\"login\":" +
                                "\"\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateUserInvalidLoginFormat() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"some@email.com\",\"login\":" +
                                "\"login with space\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateUserNullLogin() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"some@email.com\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateUserWithoutName() throws Exception {
        User expectedUser = new User(
                1L,
                "email@test.com",
                "login_test",
                "login_test",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userService.create(any())).thenReturn(expectedUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email@test.com\",\"login\":" +
                                "\"login_test\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\": 1, \"email\":\"email@test.com\",\"login\":" +
                        "\"login_test\",\"name\":\"login_test\", \"birthday\":\"1888-04-01\" }"));

    }

    @Test
    public void testCreateUserWithEmptyName() throws Exception {
        User expectedUser = new User(
                1L,
                "email@test.com",
                "login_test",
                "login_test",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userService.create(any())).thenReturn(expectedUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email@test.com\",\"login\":" +
                                "\"login_test\", \"name\":\"\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\": 1, \"email\":\"email@test.com\",\"login\":" +
                        "\"login_test\",\"name\":\"login_test\", \"birthday\":\"1888-04-01\" }"));

    }

    @Test
    public void testCreateUserNullBirthday() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email@test.com\",\"login\":" +
                                "\"login_test\", \"name\":\"Some name\" }")
                )
                .andExpect(status().isBadRequest());

    }


    @Test
    public void testCreateUserBirthdayInTheFuture() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email@test.com\",\"login\":" +
                                "\"login_test\", \"name\":\"Some name\", \"birthday\":\"2044-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateUserById() throws Exception {
        Long id = 1L;
        User updatedUser = new User(
                id,
                "new@test.com",
                "new_login_test",
                "John Wicked",
                LocalDate.of(1900, Month.APRIL, 1)
        );
        when(userService.update(updatedUser)).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\":\"new@test.com\",\"login\":" +
                                "\"new_login_test\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"new@test.com\",\"login\":" +
                        "\"new_login_test\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }"));

    }

    @Test
    public void testUpdateUserByIdNoId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"new@test.com\",\"login\":" +
                                "\"new_login_test\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateUserByIdNullableEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1,\"login\":" +
                                "\"new_login_test\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateUserByEmptyEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\":\"\", \"login\":" +
                                "\"new_login_test\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateUserByInvalidEmailFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\":\"@email@.ru\", \"login\":" +
                                "\"new_login_test\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateUserByEmptyLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\":\"new@test.com\", \"login\":" +
                                "\"\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateUserByNullableLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\":\"test@test.com\",\"name\":\"John Wicked\", " +
                                "\"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateUserByNullableName() throws Exception {
        User expectedUser = new User(
                1L,
                "test@test.com",
                "new_login",
                "some_name",
                LocalDate.of(1900, Month.APRIL, 1)
        );
        when(userService.update(any())).thenReturn(expectedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"login\": \"new_login\", \"email\":\"test@test.com\"," +
                                "\"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"test@test.com\",\"login\":" +
                        "\"new_login\",\"name\":\"some_name\", \"birthday\":\"1900-04-01\" }"));

    }

    @Test
    public void testUpdateUserByEmptyName() throws Exception {
        User expectedUser = new User(
                1L,
                "test@test.com",
                "new_login",
                "some_name",
                LocalDate.of(1900, Month.APRIL, 1)
        );
        when(userService.update(any())).thenReturn(expectedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"login\": \"new_login\", \"name\": \"\", \"email\":\"test@test.com\"," +
                                "\"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"test@test.com\",\"login\":" +
                        "\"new_login\",\"name\":\"some_name\", \"birthday\":\"1900-04-01\" }"));

    }

    @Test
    public void testUpdateUserByNullableBirthday() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"login\": \"new_login\", \"email\":\"test@test.com\"," +
                                "\"name\":\"John Wicked\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateUserByFutureBirthday() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"login\": \"new_login\", \"email\":\"test@test.com\"," +
                                "\"name\":\"John Wicked\", \"birthday\":\"2044-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testAddFriend() throws Exception {
        User expectedUser = new User(
                1L,
                "test@test.com",
                "new_login",
                "some_name",
                LocalDate.of(1900, Month.APRIL, 1)
        );
        expectedUser.addToFriend(2L);
        when(userService.addToFriends(1L, 2L)).thenReturn(expectedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"test@test.com\",\"login\":" +
                        "\"new_login\",\"name\":\"some_name\", \"birthday\":\"1900-04-01\", \"friends\": [2] }"));

    }

    @Test
    public void testAddFriendNotFound() throws Exception {
        when(userService.addToFriends(1L, 2L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1/friends/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

    }

    @Test
    public void testRemoveFriend() throws Exception {
        User expectedUser = new User(
                1L,
                "test@test.com",
                "new_login",
                "some_name",
                LocalDate.of(1900, Month.APRIL, 1)
        );
        when(userService.removeFromFriends(1L, 2L)).thenReturn(expectedUser);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1/friends/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"test@test.com\",\"login\":" +
                        "\"new_login\",\"name\":\"some_name\", \"birthday\":\"1900-04-01\", \"friends\": [] }"));

    }

    @Test
    public void testRemoveFriendNotFound() throws Exception {
        when(userService.removeFromFriends(1L, 2L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1/friends/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());

    }

    @Test
    public void testGetFriendsList() throws Exception {
        when(userService.getUserFriends(1L)).thenReturn(Set.of(2L));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\": 2 }]"));

    }

    @Test
    public void testGetFriendsListNotFound() throws Exception {
        when(userService.getUserFriends(1L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetCommonFriends() throws Exception {
        when(userService.getCommonFriends(1L, 2L)).thenReturn(Set.of(3L));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends/common/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\": 3 }]"));

    }

    @Test
    public void testGetCommonFriendsNotFound() throws Exception {
        when(userService.getCommonFriends(1L, 2L)).thenThrow(EntityNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/friends/common/2")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }
}
