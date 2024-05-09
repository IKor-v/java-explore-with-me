package ru.practicum.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.events.dto.EventDtoFull;
import ru.practicum.events.dto.EventDtoIn;
import ru.practicum.events.entity.StateEvent;
import ru.practicum.events.service.AdminEventsService;
import ru.practicum.groupvalid.UpdateInfo;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
public class AdminEventsController {
    private final AdminEventsService adminEventsService;

    @Autowired
    public AdminEventsController(AdminEventsService adminEventsService) {
        this.adminEventsService = adminEventsService;
    }

    @GetMapping
    public ResponseEntity<List<EventDtoFull>> getAdminEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                                             @RequestParam(name = "states", required = false) List<StateEvent> states,
                                                             @RequestParam(name = "categories", required = false) List<Long> categories,
                                                             @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                             @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                             @RequestParam(name = "from", defaultValue = "0") int from,
                                                             @RequestParam(name = "size", defaultValue = "10") int size) {
        List<EventDtoFull> result = adminEventsService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
        log.info("Запрос всех событий(админ)");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventDtoFull> patchEvent(@PathVariable Long eventId,
                                                   @RequestBody @Validated(UpdateInfo.class) EventDtoIn EventDto) { //Изменить DTO
        EventDtoFull result = adminEventsService.patchEvent(eventId, EventDto);
        log.info("Обновление события с id = {}", eventId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
