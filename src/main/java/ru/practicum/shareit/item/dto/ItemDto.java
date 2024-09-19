package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.response.dto.ItemResponseDto;

import java.util.List;

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
    @NotNull
    Boolean available;
    BookingDto lastBooking;
    BookingDto nextBooking;
    List<ItemResponseDto> comments;
}
