package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @MockBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    UserDto userDto;

    @BeforeEach
    void prepare() {
        userDto = UserDto.builder()
                .id(1L)
                .name("userName")
                .login("userLogin")
                .email("user@mail.ru")
                .birthday(LocalDate.now().minusYears(20))
                .build();
    }

    @Test
    void createUser() throws Exception {
        when(userService.createUser(any()))
                .thenReturn(userDto);
        mockMvc.perform(post("/users")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class));
        verify(userService, times(1)).createUser(userDto);
        verify(userService, never()).updateUser(any());
    }

    @Test
    void updateUser() throws Exception {
        when(userService.updateUser(any()))
                .thenReturn(userDto);
        mockMvc.perform(patch("/users/{userId}", userDto.getId())
                        .header("X-Sharer-User-Id", userDto.getId())
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class));
        verify(userService, times(1)).updateUser(userDto);
        verify(userService, never()).createUser(any());
    }

    @Test
    void getAllUsers() throws Exception {
        when(userService.getAllUsers())
                .thenReturn(List.of(userDto));
        mockMvc.perform(get("/users")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1), Integer.class))
                .andExpect(jsonPath("$[0].id", is(userDto.getId()), Long.class));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(anyLong()))
                .thenReturn(userDto);
        mockMvc.perform(get("/users/{userId}", userDto.getId())
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class));
        verify(userService, times(1)).getUserById(userDto.getId());
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/{userId}", userDto.getId())
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotExist());
        verify(userService, times(1)).deleteUserById(userDto.getId());
    }
}