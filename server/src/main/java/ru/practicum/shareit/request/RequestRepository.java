package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByUserId(long userId);

    List<ItemRequest> findAllByOrderByCreated();

    List<ItemRequest> findByUserIdOrderByCreated(long userId);
}
