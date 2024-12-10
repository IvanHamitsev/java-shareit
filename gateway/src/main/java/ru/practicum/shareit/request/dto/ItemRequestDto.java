package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime created;
    List<MiniItemDto> items; // Has name responseList in server
}
