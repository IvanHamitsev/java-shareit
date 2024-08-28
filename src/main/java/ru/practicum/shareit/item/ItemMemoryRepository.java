package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.DataOperationException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
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
            throw new DataOperationException("Не удалось добавить лот itemId = " + item.getId());
        }
    }

    @Override
    public Item update(Item oldItem, Item newItem) {
        patchItem(newItem, oldItem);
        if (items.remove(oldItem) && items.add(newItem)) {
            return newItem;
        } else {
            throw new DataOperationException("Не удалось обновить лот itemId = " + oldItem.getId());
        }
    }

    @Override
    public void delete(Item item) {
        items.remove(item);
    }

    @Override
    public List<Item> serchItems(String text) {
        // почему-то пустая строка считается не подходящей ни под одну сущность
        if ((text == null) || (text.isEmpty())) {
            return new ArrayList<>();
        }
        // почему-то в тестах считается регистро-независимый поиск
        return items.parallelStream()
                .filter(item ->
                        (((item.getName().toUpperCase().contains(text.toUpperCase())) ||
                                (item.getDescription().toUpperCase().contains(text.toUpperCase()))) &&
                                (item.getIsAvailableForRent()))
                )
                .toList();
    }

    public void patchItem(Item newItem, Item oldItem) {
        if (newItem.getName() == null) {
            newItem.setName(oldItem.getName());
        }
        if (newItem.getDescription() == null) {
            newItem.setDescription(oldItem.getDescription());
        }
        if (newItem.getRequest() == null) {
            newItem.setRequest(oldItem.getRequest());
        }
        if (newItem.getIsAvailableForRent() == null) {
            newItem.setIsAvailableForRent(oldItem.getIsAvailableForRent());
        }
        if (newItem.getIsRented() == null) {
            newItem.setIsRented(oldItem.getIsRented());
        }
        if (newItem.getOwner() == null) {
            newItem.setOwner(oldItem.getOwner());
        }
    }

    private Long generateItemId() {
        return ++itemId;
    }

    private void rollbackItemId() {
        itemId--;
    }

}
