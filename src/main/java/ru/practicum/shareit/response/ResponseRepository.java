package ru.practicum.shareit.response;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.response.model.ItemResponse;

import java.util.List;

public interface ResponseRepository extends JpaRepository<ItemResponse, Long> {
    // Достаточно стандартных методов JpaRepository
    List<ItemResponse> findByItemId(long itemId);
}
