package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    long id;
    String name;
    String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime created;
    List<MiniItemDto> items; // Has name responseList earlier
}
