package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> getAllByOwnerId(long ownerId);

    Optional<Item> getById(long itemId);

    Item create(Item item);

    Item update(Item oldItem, Item newItem);

    void delete(Item item);

    List<Item> serchItems(String text);
}
