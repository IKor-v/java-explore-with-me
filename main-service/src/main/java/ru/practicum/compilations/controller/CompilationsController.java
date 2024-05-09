package ru.practicum.compilations.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilations.dto.CompilationDtoOut;
import ru.practicum.compilations.service.CompilationsService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
public class CompilationsController {
    private final CompilationsService compilationsService;

    @Autowired
    public CompilationsController(CompilationsService compilationsService) {
        this.compilationsService = compilationsService;
    }

    @GetMapping
    public ResponseEntity<List<CompilationDtoOut>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                                   @RequestParam(defaultValue = "0") Integer from,
                                                                   @RequestParam(defaultValue = "10") Integer size) {
        List<CompilationDtoOut> result = compilationsService.getCompilations(pinned, from, size);
        log.info("Запрос всех подборок.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{comId}")
    public ResponseEntity<CompilationDtoOut> getCompilationById(@PathVariable Long comId) {
        CompilationDtoOut result = compilationsService.getCompilationById(comId);
        log.info("Запрос подборки с id = {}", comId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}
