package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // Почти всё из стандартного JpaRepository
    List<Item> findByOwnerId(long ownerId);

    @Query("""
            select it from Item it where
                        (UPPER(it.name) like concat('%', UPPER(?1), '%') or
            			 UPPER(it.description) like concat('%', UPPER(?1), '%')) and
                        it.isAvailableForRent = true
            """)
    List<Item> searchItems(String text);
}
