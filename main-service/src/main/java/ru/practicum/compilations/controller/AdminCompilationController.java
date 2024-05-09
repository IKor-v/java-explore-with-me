package ru.practicum.compilations.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilations.dto.CompilationDtoIn;
import ru.practicum.compilations.dto.CompilationDtoOut;
import ru.practicum.compilations.service.AdminCompilationsService;
import ru.practicum.groupvalid.CreateInfo;
import ru.practicum.groupvalid.UpdateInfo;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final AdminCompilationsService compilationsService;

    @Autowired
    public AdminCompilationController(AdminCompilationsService compilationsService) {
        this.compilationsService = compilationsService;
    }

    @PostMapping
    public ResponseEntity<CompilationDtoOut> postNewCompilation(@RequestBody @Validated(CreateInfo.class) CompilationDtoIn compilation) {
        CompilationDtoOut result = compilationsService.postNewCompilation(compilation);
        log.info("Добавление новой подборки {}", compilation);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PatchMapping("/{comId}")
    public ResponseEntity<CompilationDtoOut> patchCompilation(@PathVariable Long comId, @RequestBody @Validated(UpdateInfo.class) CompilationDtoIn compilation) {
        CompilationDtoOut result = compilationsService.patchCompilation(compilation, comId);
        log.info("Обновление подборки {}", result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{comId}")
    public ResponseEntity<Object> delCompilation(@PathVariable Long comId) {
        compilationsService.delCompilation(comId);
        log.info("Удаление подбоки с id = {}", comId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
