package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.response.dto.ItemResponseDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemMapper {
    public static Item mapItemDto(ItemDto itemDto, User owner) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .owner(owner) // объект ItemDto не знает хозяина
                .isAvailableForRent(itemDto.getAvailable())
                .build();
    }

    public static Item mapItemDto(ItemDto itemDto, User owner, ItemRequest request) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .owner(owner) // объект ItemDto не знает хозяина
                .isAvailableForRent(itemDto.getAvailable())
                .request(request)
                .build();
    }

    public static ItemDto mapItem(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailableForRent())
                .requestId(item.getRequest() == null ? null : item.getRequest().getId())
                .build();
    }

    public static ItemDto mapItem(Item item, BookingDto lastBooking, BookingDto nextBooking,
                                  List<ItemResponseDto> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailableForRent())
                .requestId(item.getRequest() == null ? null : item.getRequest().getId())
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();
    }
}
