package ru.practicum.events.service;

import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventDtoFull;
import ru.practicum.events.entity.AvailableSort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventsService {
    List<EventDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, AvailableSort sort, Integer from, Integer size, HttpServletRequest request);

    EventDtoFull getEventById(Long id, HttpServletRequest request);
}
