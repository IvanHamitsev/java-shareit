package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public List<ItemDto> getAllItems(Long userId) {
        List<Item> itemList = itemRepository.getAllByOwnerId(userId);
        return itemList.parallelStream()
                .map(ItemMapper::mapItem)
                .collect(Collectors.toList());
    }

    public ItemDto getById(long itemId) {
        return ItemMapper.mapItem(itemRepository.getById(itemId)
                .orElseThrow(() -> new NotFoundException("Лот не найден")));
    }

    public ItemDto createItem(ItemDto itemDto, long userId) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден хозяин вещи"));
        Item item = ItemMapper.mapItemDto(itemDto, user);
        return ItemMapper.mapItem(itemRepository.create(item));
    }

    public ItemDto updateItem(ItemDto itemDto, long userId) {
        Item oldItem = itemRepository.getById(itemDto.getId())
                .orElseThrow(() -> new NotFoundException("Обновляемый элемент не найден"));
        // изменения может вносить только хозяин
        testOwner(oldItem, userId);
        return ItemMapper.mapItem(itemRepository.update(oldItem, ItemMapper.mapItemDto(itemDto, oldItem.getOwner())));
    }

    public ItemDto deleteItemById(long userId, long itemId) {
        Item item = itemRepository.getById(itemId)
                .orElseThrow(() -> new NotFoundException("Удаляемый элемент не найден"));
        // удаление разрешено только хозяину
        testOwner(item, userId);
        itemRepository.delete(item);
        return ItemMapper.mapItem(item);
    }

    public List<ItemDto> searchItems(long userId, String text) {
        User user = userRepository.getById(userId)
                .orElseThrow(() -> new ValidationException("Неизвестный пользователь"));
        List<Item> itemList = itemRepository.serchItems(text);
        return itemList.parallelStream()
                .map(ItemMapper::mapItem)
                .toList();
    }

    protected void testOwner(Item item, long ownerId) {
        if (item.getOwner().getId() != ownerId) {
            throw new ForbiddenException("Изменения лота возможны только хозяином");
        }
    }
}
