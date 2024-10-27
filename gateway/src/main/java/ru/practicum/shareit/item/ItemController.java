package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.response.dto.ItemResponseDto;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("GetAllItems of userId {}", userId);
        return itemClient.getAllItemsOfUser(userId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(name = "X-Sharer-User-Id") Long userId,
                                              @PathVariable long itemId) {
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam String text) {
        return itemClient.searchItems(userId, text);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody @Valid ItemDto itemDto) {
        return itemClient.createItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createItemResponse(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @PathVariable long itemId,
                                                     @RequestBody ItemResponseDto itemResponseDto) {
        return itemClient.createResponse(userId, itemId, itemResponseDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody ItemDto itemDto,
                                             @PathVariable long itemId) {
        itemDto.setId(itemId);
        return itemClient.updateItem(userId, itemDto);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId) {
        return itemClient.deleteItemById(userId, itemId);
    }
}