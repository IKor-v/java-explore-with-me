package ru.practicum.service;

import ru.practicum.StatDto;
import ru.practicum.StatDtoOut;

import java.time.LocalDateTime;
import java.util.List;

public interface StatServerService {
    List<StatDtoOut> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);

    StatDto postStats(StatDto statDto);
}
