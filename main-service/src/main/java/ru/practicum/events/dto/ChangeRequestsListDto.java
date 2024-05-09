package ru.practicum.events.dto;

import lombok.Data;
import ru.practicum.requests.entity.RequestsStatus;

import java.util.Set;

@Data
public class ChangeRequestsListDto {
    private Set<Long> requestIds;
    private RequestsStatus status;
}
