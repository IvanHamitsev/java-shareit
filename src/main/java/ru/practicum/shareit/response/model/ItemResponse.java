package ru.practicum.shareit.response.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemResponse {
    long id;
    @NotNull
    User responseUser;
    String description;
    @NotNull
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm")
    LocalDateTime responseDate;
}
