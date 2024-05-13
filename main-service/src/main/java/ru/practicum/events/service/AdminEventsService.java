package ru.practicum.events.service;

import ru.practicum.events.dto.EventDtoFull;
import ru.practicum.events.dto.EventDtoIn;
import ru.practicum.events.entity.StateEvent;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventsService {
    EventDtoFull patchEvent(Long eventId, EventDtoIn adminEventDto);

    List<EventDtoFull> getEvents(List<Long> users, List<StateEvent> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);
}
