package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @MockBean
    ItemRequestService itemRequestService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    UserDto userDto;
    ItemDto itemDto;
    ItemRequestDto requestDto;

    @BeforeEach
    void prepare() {
        userDto = UserDto.builder()
                .id(3L)
                .name("userName")
                .login("userLogin")
                .email("user@mail.ru")
                .birthday(LocalDate.now().minusYears(20))
                .build();
        itemDto = ItemDto.builder()
                .id(4L)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .build();
        requestDto = ItemRequestDto.builder()
                .id(5L)
                .name("requestedItemName")
                .description("descriptionOfRequest")
                .created(LocalDateTime.now().minusMinutes(10))
                .build();
    }

    @Test
    void createRequest() throws Exception {
        when(itemRequestService.createRequest(anyLong(), any()))
                .thenReturn(requestDto);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .content(objectMapper.writeValueAsString(requestDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(requestDto.getName()), String.class));
        verify(itemRequestService, times(1)).createRequest(userDto.getId(), requestDto);
    }

    @Test
    void getUserRequests() throws Exception {
        when(itemRequestService.getAllUserRequests(anyLong()))
                .thenReturn(List.of(requestDto));
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1), Integer.class))
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class));
        verify(itemRequestService, times(1)).getAllUserRequests(userDto.getId());
    }

    @Test
    void getRequestById() throws Exception {
        when(itemRequestService.getRequestById(anyLong()))
                .thenReturn(requestDto);
        mockMvc.perform(get("/requests/{requestId}", requestDto.getId())
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(requestDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(requestDto.getName()), String.class));
        verify(itemRequestService, times(1)).getRequestById(requestDto.getId());
    }

    @Test
    void getAllRequests() throws Exception {
        when(itemRequestService.getAllRequests())
                .thenReturn(List.of(requestDto));
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1), Integer.class))
                .andExpect(jsonPath("$[0].id", is(requestDto.getId()), Long.class));
        verify(itemRequestService, times(1)).getAllRequests();
    }
}