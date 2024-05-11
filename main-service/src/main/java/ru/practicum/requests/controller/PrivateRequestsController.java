package ru.practicum.requests.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.service.PrivateRequestsService;

import java.util.List;

/**
 * Контроллер для запросов от авторизованных пользователей.
 * Позволяет посмотреть все свои запросы, добавить запрос или отменить его.
 */
@Slf4j
@Controller
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateRequestsController {
    /**
     * Статичная переменная для интерфейса сервиса запросов от авторизованных пользователей.
     */
    private final PrivateRequestsService privateRequestsService;

    /**
     * Конструктор для контроллера запросов от авторизованных пользователей.
     *
     * @param privateRequestsService передает сервис запросов от авторизованных пользователей
     */
    @Autowired
    public PrivateRequestsController(PrivateRequestsService privateRequestsService) {
        this.privateRequestsService = privateRequestsService;
    }

    /**
     * Возвращает список всех запросов на участие пользователя в чужих событиях
     *
     * @param userId Id пользователя
     * @return Список всех запросов на участие в статусе 200
     */
    @GetMapping
    public ResponseEntity<List<RequestDto>> getRequests(@PathVariable Long userId) {
        List<RequestDto> result = privateRequestsService.getRequests(userId);
        log.info("Просмотр всех запросов на участие от пользователя с id = {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * Создает запрос от пользователя на участие в чужом событии
     *
     * @param userId  Id пользователя
     * @param eventId Id события для участия
     * @return Созадный запрос в статусе 201
     */
    @PostMapping
    public ResponseEntity<RequestDto> postRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        RequestDto result = privateRequestsService.postRequests(userId, eventId);
        log.info("Составлен запрос на участие от пользователя с id = {} в событии с id = {}", userId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Отменяет запрос от пользователя на участие в чужом событии
     *
     * @param userId    Id пользователя
     * @param requestId Id события
     * @return Отмененный запрос в статусе 200
     */
    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestDto> patchRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        RequestDto result = privateRequestsService.cancelRequests(userId, requestId);
        log.info("Отмена запроса с id = {} на участие от пользователя с id = {}", requestId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
