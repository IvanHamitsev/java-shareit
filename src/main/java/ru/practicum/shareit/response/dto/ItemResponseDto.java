package ru.practicum.shareit.response.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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
public class ItemResponseDto {
    long id;
    ItemDto item;
    UserDto responseUser;
    String authorName;
    String name;
    @NotNull
    String text;
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime created;
}
