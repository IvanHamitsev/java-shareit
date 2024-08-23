package ru.practicum.shareit.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public ErrorResponse notFoundExceptionHandler(final NotFoundException exception) {
        log.info(exception.getMessage(), exception);
        return new ErrorResponse("error", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse notFoundExceptionHandler(final DataOperationException exception) {
        log.info(exception.getMessage(), exception);
        return new ErrorResponse("error", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse badRequest(final ValidationException exception) {
        log.info(exception.getMessage(), exception);
        return new ErrorResponse("error", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse internalServerExceptionHandler(final Exception exception) {
        log.warn(Arrays.toString(exception.getStackTrace()));
        return new ErrorResponse("", exception.getMessage());
    }
}

@Data
class ErrorResponse {
    private final String error;
    private final String description;
}
