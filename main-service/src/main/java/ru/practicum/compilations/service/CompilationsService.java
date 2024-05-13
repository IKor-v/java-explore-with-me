package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationDtoOut;

import java.util.List;

public interface CompilationsService {
    List<CompilationDtoOut> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDtoOut getCompilationById(Long comId);
}
