package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.BookingStatusType;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTest {
    private final JacksonTester<BookingDto> json;

    @Test
    void testBookingDto() throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        LocalDateTime start = LocalDateTime.now().minusMinutes(10);
        LocalDateTime end = LocalDateTime.now().minusMinutes(5);
        UserDto userDto = UserDto.builder()
                .id(1L)
                .name("name")
                .login("login")
                .email("email@mail.com")
                .build();
        ItemDto itemDto = ItemDto.builder()
                .id(2L)
                .name("itemName")
                .description("itemDescription")
                .available(true)
                .build();
        BookingDto bookingDto = BookingDto.builder()
                .id(3L)
                .booker(userDto)
                .item(itemDto)
                .itemId(itemDto.getId())
                .status(BookingStatusType.APPROVED.toString())
                .start(start)
                .end(end)
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(3);

        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(userDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.booker.login").isEqualTo(userDto.getLogin());
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo(userDto.getEmail());

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(itemDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo(itemDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(itemDto.getAvailable());

        // есть особенность, формат LocalDateTime, приходящий в ответе теста в локальной IDEA содержит 9 символов после
        // запятой в долях секунды, а на github 7. Поэтому локально использую DateTimeFormatter с указанием 7 символов,
        // а на github без DateTimeFormatter
        /*assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(dtf.format(bookingDto.getStart()));
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(dtf.format(bookingDto.getEnd()));*/

        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingDto.getEnd().toString());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo(bookingDto.getStatus());
    }
}