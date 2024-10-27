package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
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
    long id;
    String name;
    String login;
    @Email
    String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;
}
