package ru.practicum.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.events.dto.ChangeRequestsListDto;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventDtoFull;
import ru.practicum.events.dto.EventDtoIn;
import ru.practicum.events.service.PrivateEventsService;
import ru.practicum.groupvalid.CreateInfo;
import ru.practicum.groupvalid.UpdateInfo;
import ru.practicum.requests.dto.RequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventsController {
    private final PrivateEventsService privateEventsService;

    @Autowired
    public PrivateEventsController(PrivateEventsService privateEventsService) {
        this.privateEventsService = privateEventsService;
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getPrivateEvents(@PathVariable Long userId,
                                                           @RequestParam(defaultValue = "0") Integer from,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        List<EventDto> result = privateEventsService.getEventsForUser(userId, from, size);
        log.info("Запрос событий, созданных пользователем с id = {}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDtoFull> getEventById(@PathVariable Long userId, @PathVariable Long eventId) {
        EventDtoFull result = privateEventsService.getEventForUserById(userId, eventId);
        log.info("Запрос события с id = {}, созданного пользователем с id = {}", eventId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping
    public ResponseEntity<EventDtoFull> postEvent(@PathVariable Long userId,
                                                  @Validated(CreateInfo.class) @RequestBody EventDtoIn eventDtoIn) {
        EventDtoFull result = privateEventsService.postNewEvent(userId, eventDtoIn);
        log.info("Создание события от пользователя с id = {}", userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventDtoFull> patchEvent(@PathVariable Long userId, @PathVariable Long eventId,
                                                   @Validated(UpdateInfo.class) @RequestBody EventDtoIn eventDtoIn) {
        EventDtoFull result = privateEventsService.patchEvent(userId, eventId, eventDtoIn);
        log.info("Изменения события с id = {} от пользователя с id = {}", eventId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getEventRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        List<RequestDto> result = privateEventsService.getRequestsForEvent(userId, eventId);
        log.info("Просмотр заявок пользователю с id = {}, на участие в событии с id = {}", userId, eventId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> pathEventRequests(@PathVariable Long userId,
                                                              @PathVariable Long eventId,
                                                              @RequestBody ChangeRequestsListDto requestsList) {
        List<RequestDto> result = privateEventsService.changeResultStatus(requestsList, userId, eventId);
        log.info("Пользователь {} меняет статус для запросов {} на событие {} статус {}", userId, requestsList.getRequestIds(), eventId, requestsList.getStatus());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
