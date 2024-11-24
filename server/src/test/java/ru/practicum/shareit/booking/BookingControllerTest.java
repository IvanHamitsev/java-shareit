package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.RequestType;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    UserDto userDto;
    ItemDto itemDto;
    BookingDto bookingDto;

    @BeforeEach
    void prepare() {
        userDto = UserDto.builder()
                .id(1L)
                .name("userName")
                .login("userLogin")
                .email("user@mail.ru")
                .birthday(LocalDate.now().minusYears(20))
                .build();
        itemDto = ItemDto.builder()
                .id(1L)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .build();
        bookingDto = BookingDto.builder()
                .id(1L)
                .booker(userDto)
                .item(itemDto)
                .status("WAITING")
                .start(LocalDateTime.now().minusDays(2))
                .end(LocalDateTime.now().minusDays(1))
                .build();
    }

    @Test
    void createBooking() throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        long userId = 1L;
        when(bookingService.createBooking(any(), anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName()), String.class))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName()), String.class))
                // есть особенность, формат LocalDateTime, приходящий в ответе теста в локальной IDEA содержит 9 символов после
                // запятой в долях секунды, а на github 7. Поэтому локально использую DateTimeFormatter с указанием 7 символов,
                // а на github без DateTimeFormatter
                /*
                .andExpect(jsonPath("$.start", is(dtf.format(bookingDto.getStart())), String.class))
                .andExpect(jsonPath("$.end", is(dtf.format(bookingDto.getEnd())), LocalDateTime.class));
                */
                .andExpect(jsonPath("$.start", is(bookingDto.getStart().toString()), String.class))
                .andExpect(jsonPath("$.end", is(bookingDto.getEnd().toString()), LocalDateTime.class));

        verify(bookingService, times(1)).createBooking(bookingDto, userId);
    }

    @Test
    void patchBooking() throws Exception {
        when(bookingService.changeBookingStatus(anyLong(), anyLong(), eq(true)))
                .thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/{bookingId}", bookingDto.getId()) // path variable
                        .param("approved", "true") // request param
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName()), String.class))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName()), String.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus()), String.class));

        verify(bookingService, times(1)).changeBookingStatus(bookingDto.getBooker().getId(), bookingDto.getId(), true);
        verify(bookingService, never()).createBooking(any(), anyLong());
    }

    @Test
    void getBooking() throws Exception {
        long userId = 1L;

        when(bookingService.getBookingInfo(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/{bookingId}", bookingDto.getId())
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.booker.name", is(bookingDto.getBooker().getName()), String.class))
                .andExpect(jsonPath("$.item.name", is(bookingDto.getItem().getName()), String.class));

        verify(bookingService, times(1)).getBookingInfo(userId, bookingDto.getId());
    }

    @Test
    void getAllUserBookings() throws Exception {
        long userId = 1L;
        List<BookingDto> bookings = List.of(bookingDto);

        when(bookingService.getAllUserBookings(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1L), Long.class))
                .andExpect(jsonPath("$[0].id", is(bookings.getFirst().getId()), Long.class));

        verify(bookingService, times(1)).getAllUserBookings(userId, RequestType.ALL, 0, 10);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "CURRENT")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getAllUserBookings(userId, RequestType.CURRENT, 0, 10);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "PAST")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getAllUserBookings(userId, RequestType.PAST, 0, 10);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "FUTURE")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getAllUserBookings(userId, RequestType.FUTURE, 0, 10);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "WAITING")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getAllUserBookings(userId, RequestType.WAITING, 0, 10);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "REJECTED")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService, times(1)).getAllUserBookings(userId, RequestType.REJECTED, 0, 10);

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .param("state", "WRONG_STATE")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

    }

    @Test
    void getAllOwnerBooking() throws Exception {
        long userId = 1L;
        List<BookingDto> bookings = List.of(bookingDto);

        when(bookingService.getAllOwnerBookings(anyLong(), any()))
                .thenReturn(bookings);

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", userId)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(1L), Long.class))
                .andExpect(jsonPath("$[0].id", is(bookings.getFirst().getId()), Long.class));

        verify(bookingService, times(1)).getAllOwnerBookings(userId, RequestType.ALL);
    }
}