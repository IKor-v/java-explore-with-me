package ru.practicum.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.StatDto;
import ru.practicum.StatDtoOut;
import ru.practicum.service.StatServerService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
public class StatServerController {
    private final StatServerService statService;

    @Autowired
    public StatServerController(StatServerService statService) {
        this.statService = statService;

    }

    @GetMapping("/stats")
    public ResponseEntity<List<StatDtoOut>> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                     @RequestParam(defaultValue = "") List<String> uris,
                                                     @RequestParam(defaultValue = "false") boolean unique) {
        List<StatDtoOut> result = statService.getStats(start, end, uris, unique);
        log.info("Получаю статистику за период с {}, по {}", start, end);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/hit")
    public ResponseEntity<StatDto> postStats(@RequestBody @Valid StatDto statDto) {
        StatDto result = statService.postStats(statDto);
        log.info("Сохраняю статистику для {}", statDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


}
