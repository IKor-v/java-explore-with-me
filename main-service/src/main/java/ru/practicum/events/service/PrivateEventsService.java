package ru.practicum.events.service;

import ru.practicum.events.dto.ChangeRequestsListDto;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventDtoFull;
import ru.practicum.events.dto.EventDtoIn;
import ru.practicum.requests.dto.RequestDto;

import java.util.List;

public interface PrivateEventsService {
    List<EventDto> getEventsForUser(Long userId, Integer from, Integer size);

    EventDtoFull getEventForUserById(Long userId, Long eventId);

    List<RequestDto> getRequestsForEvent(Long userId, Long eventId);

    EventDtoFull postNewEvent(Long userId, EventDtoIn eventDtoIn);

    EventDtoFull patchEvent(Long userId, Long eventId, EventDtoIn eventDtoIn);

    List<RequestDto> changeResultStatus(ChangeRequestsListDto requestsList, Long userId, Long eventId);
}
