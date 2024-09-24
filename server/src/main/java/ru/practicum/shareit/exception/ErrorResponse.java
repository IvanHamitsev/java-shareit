package ru.practicum.shareit.exception;

import lombok.Data;

@Data
class ErrorResponse {
    private final String error;
    private final String description;
}
