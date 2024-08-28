package ru.practicum.shareit.booking.model;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    long id;
    @NotNull
    User user;
    @NotNull
    Item item;
    @NotNull
    BookingStatusType status;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime bookingStart;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime bookingEnd;
}
