package ru.practicum.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventDtoFull;
import ru.practicum.events.entity.AvailableSort;
import ru.practicum.events.service.PublicEventsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
public class PublicEventsController {
    private final PublicEventsService publicEventsService;

    @Autowired
    public PublicEventsController(PublicEventsService publicEventsService) {
        this.publicEventsService = publicEventsService;
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getEvents(@RequestParam(name = "text", required = false) String text,
                                                    @RequestParam(name = "categories", required = false) List<Long> categories,
                                                    @RequestParam(name = "paid", required = false) Boolean paid,
                                                    @RequestParam(name = "rangeStart", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                    @RequestParam(name = "rangeEnd", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                    @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                                    @RequestParam(name = "sort", defaultValue = "EVENT_DATE") AvailableSort sort,
                                                    @RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size,
                                                    HttpServletRequest request) {
        List<EventDto> result = publicEventsService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, request);
        log.info("Запрос всех событий");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDtoFull> getEventById(@PathVariable Long id, HttpServletRequest request) {
        EventDtoFull result = publicEventsService.getEventById(id, request);
        log.info("Запрос события с id = {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
