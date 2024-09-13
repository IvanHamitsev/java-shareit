package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // Почти всё из стандартного JpaRepository
    List<Item> findByOwnerId(long ownerId);

    @Query("""
            select Item from Item where
                        (name like concat('%', ?1, '%') or
            			description like concat('%', ?1, '%')) and
                        isAvailableForRent = true
            """)
    List<Item> searchItems(String text);
}
