package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    long id;
    @NotBlank
    String name;
    String description;
    @NotNull
    User owner;
    // если вещь добавлена по запросу
    ItemRequest request;
    @NotNull
    Boolean isAvailableForRent;
    @NotNull
    Boolean isRented;
}
