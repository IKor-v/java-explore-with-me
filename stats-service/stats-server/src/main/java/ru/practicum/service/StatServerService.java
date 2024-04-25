package ru.practicum.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.StatDto;
import ru.practicum.StatDtoOut;

import java.time.LocalDateTime;
import java.util.List;

public interface StatServerService {
    ResponseEntity<List<StatDtoOut>> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    ResponseEntity<StatDto> postStats(StatDto statDto);
}
