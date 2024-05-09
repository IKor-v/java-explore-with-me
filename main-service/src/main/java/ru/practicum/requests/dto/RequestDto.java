package ru.practicum.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.dto.EventMapper;
import ru.practicum.requests.entity.RequestsStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestDto {
    private Long id;
    private RequestsStatus status = RequestsStatus.PENDING;
    private String created = LocalDateTime.now().format(EventMapper.formatter);
    private Long event;
    private Long requester;
}
