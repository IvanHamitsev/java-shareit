package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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
    @NotBlank
    String name;
    @Email
    String email;
    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate birthday;
}
