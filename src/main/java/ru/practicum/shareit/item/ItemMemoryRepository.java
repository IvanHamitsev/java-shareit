package ru.practicum.shareit.item;

import ru.practicum.shareit.exception.DataOperationException;
import ru.practicum.shareit.item.model.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ItemMemoryRepository implements ItemRepository {
    Set<Item> items = new HashSet<>();
    private Long itemId = 0L;

    @Override
    public List<Item> getAllByOwnerId(long ownerId) {
        return items.stream()
                .filter(item -> ownerId == item.getOwner().getId())
                .toList();
    }

    @Override
    public Optional<Item> getById(long itemId) {
        return items.parallelStream()
                .filter(item -> item.getId() == itemId)
                .findFirst();
    }

    @Override
    public Item create(Item item) {
        item.setId(generateItemId());
        if (items.add(item)) {
            return item;
        } else {
            rollbackItemId();
            throw new DataOperationException("Не удалось добавить лот.");
        }
    }

    @Override
    public Item update(Item oldItem, Item newItem) {
        if (items.remove(oldItem) && items.add(newItem)) {
            return newItem;
        } else {
            throw new DataOperationException("Не удалось обновить лот");
        }
    }

    @Override
    public void delete(long itemId) {

    }

    private Long generateItemId() {
        return ++itemId;
    }

    private void rollbackItemId() {
        itemId--;
    }

}
