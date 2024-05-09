package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.events.dto.EventMapper;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorApi errorValidation(final ValidationException e) {
        return ErrorApi.builder()
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT.name())
                .reason("Объект не прошел валидацию.")
                .timestamp(LocalDateTime.now().format(EventMapper.formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorApi incorrectRequest(final NotFoundException e) {
        return ErrorApi.builder()
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.name())
                .reason("Искомый объект не был найден.")
                .timestamp(LocalDateTime.now().format(EventMapper.formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorApi otherException(final RuntimeException e) {
        return ErrorApi.builder()
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.name())
                .reason("Неправильно составлен запрос.")
                .timestamp(LocalDateTime.now().format(EventMapper.formatter))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorApi conflictException(final ConflictException e) {
        return ErrorApi.builder()
                .message(e.getMessage())
                .status(HttpStatus.CONFLICT.name())
                .reason("Не выполены условия для запрошеной операции")
                .timestamp(LocalDateTime.now().format(EventMapper.formatter))
                .build();
    }
}
