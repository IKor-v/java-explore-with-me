package ru.practicum.requests.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.events.dto.EventMapper;
import ru.practicum.events.entity.Event;
import ru.practicum.requests.entity.Request;
import ru.practicum.users.entity.User;

import java.time.LocalDateTime;

@UtilityClass
public class RequestMapper {
    public Request toRequest(RequestDto requestDto, User requester, Event event) {
        return Request.builder()
                .id(requestDto.getId())
                .status(requestDto.getStatus())
                .created(LocalDateTime.parse(requestDto.getCreated(), EventMapper.formatter))
                .requester(requester)
                .event(event)
                .build();
    }

    public RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .status(request.getStatus())
                .created(request.getCreated().format(EventMapper.formatter))
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .build();
    }
}
