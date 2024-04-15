package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsersController.class)
public class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private IdGenerator idGenerator;

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
        when(userRepository.findBy(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"email@test.com\",\"login\":" +
                        "\"login_test\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }"));

    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        Long id = 1L;
        when(userRepository.findBy(id)).thenReturn(Optional.empty());

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
        when(userRepository.findAll()).thenReturn(List.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\": 1, \"email\":\"email@test.com\",\"login\":" +
                        "\"login_test\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }]"));

    }

    @Test
    public void testCreateUserSuccessfully() throws Exception {
        when(idGenerator.generateId()).thenReturn(1L);

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
        when(idGenerator.generateId()).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\",\"login\":" +
                                "\"login_test\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateUserInvalidEmailFormat() throws Exception {
        when(idGenerator.generateId()).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"@email@email.ru\",\"login\":" +
                                "\"login_test\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateUserNullEmail() throws Exception {
        when(idGenerator.generateId()).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\":" +
                                "\"login_test\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateUserEmptyLogin() throws Exception {
        when(idGenerator.generateId()).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"some@email.com\",\"login\":" +
                                "\"\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateUserInvalidLoginFormat() throws Exception {
        when(idGenerator.generateId()).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"some@email.com\",\"login\":" +
                                "\"login with space\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateUserNullLogin() throws Exception {
        when(idGenerator.generateId()).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"some@email.com\",\"name\":\"John Wick\", \"birthday\":\"1888-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testCreateUserWithoutName() throws Exception {
        when(idGenerator.generateId()).thenReturn(1L);

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
        when(idGenerator.generateId()).thenReturn(1L);

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
        when(idGenerator.generateId()).thenReturn(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"email@test.com\",\"login\":" +
                                "\"login_test\", \"name\":\"Some name\" }")
                )
                .andExpect(status().isBadRequest());

    }


    @Test
    public void testCreateUserBirthdayInTheFuture() throws Exception {
        when(idGenerator.generateId()).thenReturn(1L);

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
        User user = new User(
                id,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userRepository.findBy(id)).thenReturn(Optional.of(user));

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
        Long id = 1L;
        User user = new User(
                id,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userRepository.findBy(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"new@test.com\",\"login\":" +
                                "\"new_login_test\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateUserByIdNullableEmail() throws Exception {
        Long id = 1L;
        User user = new User(
                id,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userRepository.findBy(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1,\"login\":" +
                                "\"new_login_test\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"email@test.com\",\"login\":" +
                        "\"new_login_test\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }"));

    }

    @Test
    public void testUpdateUserByEmptyEmail() throws Exception {
        Long id = 1L;
        User user = new User(
                id,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userRepository.findBy(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\":\"\", \"login\":" +
                                "\"new_login_test\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"email@test.com\",\"login\":" +
                        "\"new_login_test\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }"));

    }

    @Test
    public void testUpdateUserByInvalidEmailFormat() throws Exception {
        Long id = 1L;
        User user = new User(
                id,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userRepository.findBy(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\":\"@email@.ru\", \"login\":" +
                                "\"new_login_test\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateUserByEmptyLogin() throws Exception {
        Long id = 1L;
        User user = new User(
                id,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userRepository.findBy(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\":\"new@test.com\", \"login\":" +
                                "\"\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testUpdateUserByNullableLogin() throws Exception {
        Long id = 1L;
        User user = new User(
                id,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userRepository.findBy(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"email\":\"test@test.com\",\"name\":\"John Wicked\", " +
                                "\"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"test@test.com\",\"login\":" +
                        "\"login_test\",\"name\":\"John Wicked\", \"birthday\":\"1900-04-01\" }"));

    }

    @Test
    public void testUpdateUserByNullableName() throws Exception {
        Long id = 1L;
        User user = new User(
                id,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userRepository.findBy(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"login\": \"new_login\", \"email\":\"test@test.com\"," +
                                "\"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"test@test.com\",\"login\":" +
                        "\"new_login\",\"name\":\"John Wick\", \"birthday\":\"1900-04-01\" }"));

    }

    @Test
    public void testUpdateUserByEmptyName() throws Exception {
        Long id = 1L;
        User user = new User(
                id,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userRepository.findBy(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"login\": \"new_login\", \"email\":\"test@test.com\"," +
                                "\"name\":\"\", \"birthday\":\"1900-04-01\" }")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"test@test.com\",\"login\":" +
                        "\"new_login\",\"name\":\"John Wick\", \"birthday\":\"1900-04-01\" }"));

    }

    @Test
    public void testUpdateUserByNullableBirthday() throws Exception {
        Long id = 1L;
        User user = new User(
                id,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userRepository.findBy(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"login\": \"new_login\", \"email\":\"test@test.com\"," +
                                "\"name\":\"John Wicked\" }")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"test@test.com\",\"login\":" +
                        "\"new_login\",\"name\":\"John Wicked\", \"birthday\":\"1888-04-01\" }"));

    }

    @Test
    public void testUpdateUserByFutureBirthday() throws Exception {
        Long id = 1L;
        User user = new User(
                id,
                "email@test.com",
                "login_test",
                "John Wick",
                LocalDate.of(1888, Month.APRIL, 1)
        );
        when(userRepository.findBy(id)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 1, \"login\": \"new_login\", \"email\":\"test@test.com\"," +
                                "\"name\":\"John Wicked\", \"birthday\":\"2044-04-01\" }")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\": 1, \"email\":\"test@test.com\",\"login\":" +
                        "\"new_login\",\"name\":\"John Wicked\", \"birthday\":\"1888-04-01\" }"));

    }
}
