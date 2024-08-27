package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.model.BookingStatusType;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    long id;
    @NotNull
    UserDto user;
    @NotNull
    ItemDto item;
    @NotNull
    BookingStatusType status;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime bookingStart;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime bookingEnd;
}
