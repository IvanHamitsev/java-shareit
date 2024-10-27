package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemRequestMapper {
    public static ItemRequest mapItemRequestDto(ItemRequestDto itemRequestDto, User user) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .name(itemRequestDto.getName())
                .description(itemRequestDto.getDescription())
                .created(itemRequestDto.getCreated())
                .user(user)
                .build();
    }

    public static ItemRequestDto mapItemRequest(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .name(itemRequest.getName())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequestDto mapItemRequest(ItemRequest itemRequest, List<MiniItemDto> responseList) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .name(itemRequest.getName())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(responseList)
                .build();
    }

    public static MiniItemDto mapItem(Item item) {
        return new MiniItemDto(item.getId(), item.getName(), item.getOwner().getId());
    }
}
