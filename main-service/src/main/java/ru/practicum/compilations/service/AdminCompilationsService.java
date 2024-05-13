package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationDtoIn;
import ru.practicum.compilations.dto.CompilationDtoOut;

public interface AdminCompilationsService {
    CompilationDtoOut postNewCompilation(CompilationDtoIn compilation);

    CompilationDtoOut patchCompilation(CompilationDtoIn compilation, Long comId);

    void delCompilation(Long comId);
}
