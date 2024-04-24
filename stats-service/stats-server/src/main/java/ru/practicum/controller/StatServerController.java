package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.StatDto;
import ru.practicum.StatDtoOut;
import ru.practicum.service.StatServerService;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
public class StatServerController {
    private final StatServerService statService;

    @Autowired
    public StatServerController(StatServerService statService) {
        this.statService = statService;

    }

    @GetMapping("/stats")   // stats?start="2024-01-01 01:01:01"&end="2025-01-01 01:01:01"
    public ResponseEntity<List<StatDtoOut>> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")  LocalDateTime start,
                                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                     @RequestParam(defaultValue = "") List<String> uris,
                                                     @RequestParam(defaultValue = "false") boolean unique) {
        ResponseEntity<List<StatDtoOut>> result = statService.getStats(start, end, uris, unique);
        log.info("Получаю статистику за период с {}, по {}", start, end);
        return result;
    }

    @PostMapping("/hit")
    public ResponseEntity<StatDto> postStats(@RequestBody StatDto statDto) {
        ResponseEntity<StatDto> result = statService.postStats(statDto);
        log.info("Сохраняю статистику для {}", statDto);
        return result;
    }


}
