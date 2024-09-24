package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
        return new ErrorResponse("entity not found", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse notFoundExceptionHandler(final DataOperationException exception) {
        log.info(exception.getMessage(), exception);
        return new ErrorResponse("invalid request", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ErrorResponse badRequest(final ValidationException exception) {
        log.info(exception.getMessage(), exception);
        return new ErrorResponse("conflict properties", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public ErrorResponse forbiddenRequest(final ForbiddenException exception) {
        log.info(exception.getMessage(), exception);
        return new ErrorResponse("action is prohibited", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ErrorResponse validationFail(final MethodArgumentNotValidException exception) {
        log.info(exception.getMessage(), exception);
        return new ErrorResponse("invalid object properties in request", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler
    public ErrorResponse validationFail(final DataIntegrityViolationException exception) {
        log.info(exception.getMessage(), exception);
        return new ErrorResponse("conflict object properties in request", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse internalServerExceptionHandler(final Exception exception) {
        log.warn(Arrays.toString(exception.getStackTrace()));
        return new ErrorResponse("unexpected server error " + exception.getClass().getName(), exception.getMessage());
    }
}
