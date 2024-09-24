package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    long id;
    UserDto booker; // на входе пользователь не заполнен, а передан в виде id в заголовке X-Sharer-User-Id
    long itemId; // тест postman передаёт объект DTO с полем itemId
    ItemDto item; // при этом тест postman ожидает получения объекта DTO с полем item.id, item.name
    String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime start;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime end;
}
