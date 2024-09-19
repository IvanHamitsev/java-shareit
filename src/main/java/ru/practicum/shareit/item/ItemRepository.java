package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    // Почти всё из стандартного JpaRepository
    List<Item> findByOwnerId(long ownerId);

    @Query("""
            SELECT it FROM Item it WHERE
                        (UPPER(it.name) LIKE concat('%', UPPER(?1), '%') OR
            			 UPPER(it.description) LIKE concat('%', UPPER(?1), '%')) AND
                        it.isAvailableForRent = true
            """)
    List<Item> searchItems(String text);
}
