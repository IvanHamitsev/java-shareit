package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.request.model.ItemRequest;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    // если вещь добавлена по запросу
    ItemRequest request;
    // сколько раз вещь была в аренде. Пока не задано в тестах
    //@PositiveOrZero
    //int rantedCount = 0;
    @NotNull
    Boolean available;
}
