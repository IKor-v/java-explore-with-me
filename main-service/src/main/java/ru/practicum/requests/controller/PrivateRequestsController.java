package ru.practicum.requests.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.service.PrivateRequestsService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateRequestsController {
    private final PrivateRequestsService privateRequestsService;

    @Autowired
    public PrivateRequestsController(PrivateRequestsService privateRequestsService) {
        this.privateRequestsService = privateRequestsService;
    }

    @GetMapping
    public ResponseEntity<List<RequestDto>> getRequests(@PathVariable Long userId) {  //Все запросы на участие пользователя в чужих событиях
        List<RequestDto> result = privateRequestsService.getRequests(userId);
        log.info("Просмотр всех запросов на участие от пользователя с id = {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<RequestDto> postRequest(@PathVariable Long userId, @RequestParam Long eventId) { //Добавление запроса на участие
        RequestDto result = privateRequestsService.postRequests(userId, eventId);
        log.info("Составлен запрос на участие от пользователя с id = {} в событии с id = {}", userId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestDto> patchRequest(@PathVariable Long userId, @PathVariable Long requestId) {  //отмена своего участия в событии
        RequestDto result = privateRequestsService.cancelRequests(userId, requestId);
        log.info("Отмена запроса с id = {} на участие от пользователя с id = {}", requestId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
