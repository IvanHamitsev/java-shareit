package ru.practicum.shareit.response.dto;

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.response.model.ItemResponse;
import ru.practicum.shareit.user.dto.UserMapper;

public class ItemResponseMapper {
    public static ItemResponse mapItemResponseDto(ItemResponseDto itemResponseDto, Item item) {
        return ItemResponse.builder()
                .item(item)
                .name(itemResponseDto.getName())
                .description(itemResponseDto.getText())
                .responseUser(UserMapper.mapUserDto(itemResponseDto.getResponseUser()))
                .responseDate(itemResponseDto.getCreated())
                .build();
    }

    public static ItemResponseDto mapItemResponse(ItemResponse itemResponse) {
        return ItemResponseDto.builder()
                .item(ItemMapper.mapItem(itemResponse.getItem()))
                .responseUser(UserMapper.mapUser(itemResponse.getResponseUser()))
                .authorName(itemResponse.getResponseUser().getName())
                .created(itemResponse.getResponseDate())
                .name(itemResponse.getName())
                .text(itemResponse.getDescription())
                .build();
    }
}
