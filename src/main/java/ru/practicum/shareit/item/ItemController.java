package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.response.ResponseService;
import ru.practicum.shareit.response.dto.ItemResponseDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    final ItemService itemService;
    final ResponseService responseService;

    @GetMapping
    public List<ItemDto> getAllItemsOfUsers(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId,
                           @PathVariable long itemId) {
        if (null == userId) {
            throw new ValidationException("Запрос не содержит данных о авторизации пользователя");
        }
        return itemService.getById(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text) {
        var resultItemDtoList = itemService.searchItems(userId, text);
        return resultItemDtoList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        ItemDto resultItemDto = itemService.createItem(itemDto, userId);
        return resultItemDto;
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponseDto createItemResponse(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                                              @Valid @RequestBody ItemResponseDto itemResponseDto) {
        ItemResponseDto resultItemResponseDto = responseService.createResponse(itemResponseDto, userId, itemId);
        return resultItemResponseDto;
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody ItemDto itemDto,
                              @PathVariable long itemId) {
        itemDto.setId(itemId);
        ItemDto resultItemDto = itemService.updateItem(itemDto, userId);
        return resultItemDto;
    }

    @DeleteMapping("/{itemId}")
    public ItemDto deleteItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        return itemService.deleteItemById(userId, itemId);
    }
}
