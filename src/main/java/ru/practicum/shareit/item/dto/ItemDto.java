package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
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
    String description;
    // если вещь добавлена по запросу
    ItemRequest request;
    // сколько раз вещь была в аренде. Пока не задано в тестах
    //@PositiveOrZero
    //int rantedCount = 0;
    @NotNull
    Boolean available;
}
