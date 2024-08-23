package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    final ItemService itemService;

    @GetMapping
    public List<UserDto> getAllItemsOfUsers(@RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId) {
        if (null == userId) {
            throw new ValidationException("Запрос не содержит данных о авторизации пользователя");
        }
        return itemService.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    public UserDto getById(@RequestHeader(name = "X-Sharer-User-Id", required = false) Long userId,
                           @PathVariable long itemId) {
        if (null == userId) {
            throw new ValidationException("Запрос не содержит данных о авторизации пользователя");
        }
        return itemService.getById(itemId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createItem(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PutMapping
    public UserDto updateUser(@Valid @RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/{userId}")
    public UserDto deleteUser(@PathVariable long userId) {
        return userService.deleteUserById(userId);
    }
}
