package ru.practicum.shareit.user.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    long id = 0; // акцентирую внимание, что id не заполнен
    String name;
    String login;
    String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;
}
