package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    final ItemService itemService;

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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto, userId);
    }

    @PutMapping
    public ItemDto updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        return itemService.updateItem(itemDto, userId);
    }

    @DeleteMapping("/{itemId}")
    public ItemDto deleteItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        return itemService.deleteItemById(userId, itemId);
    }
}
