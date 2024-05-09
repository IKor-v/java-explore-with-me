package ru.practicum.requests.service;

import ru.practicum.requests.dto.RequestDto;

import java.util.List;

public interface PrivateRequestsService {
    List<RequestDto> getRequests(Long userId);

    RequestDto postRequests(Long userId, Long eventId);

    RequestDto cancelRequests(Long userId, Long requestId);
}
