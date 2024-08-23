package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class ItemMapper {
    public static Item mapItemDto(ItemDto itemDto, User owner) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .owner(owner) // объект ItemDto не знает хозяина
                .isAvailableForRent(itemDto.getAvailable())
                .isRented(false) // вновь созданная вещь не занята
                .request(itemDto.getRequest())
                .build();
    }

    public static ItemDto mapItem(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getIsAvailableForRent()) // пока не учитываем isRented
                .request(item.getRequest())
                .build();
    }
}
