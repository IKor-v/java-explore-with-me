package ru.practicum.requests.dto;

import lombok.Data;
import ru.practicum.requests.entity.RequestsStatus;

import java.util.Set;

@Data
public class EventRequestStatusUpdateDtoIn {
    private Set<Long> requestIds;
    private RequestsStatus status;
}
