package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Item {
    long id;
    @NotBlank
    String name;
    @NotBlank
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
