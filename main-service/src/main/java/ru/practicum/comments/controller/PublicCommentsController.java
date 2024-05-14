package ru.practicum.comments.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.service.PublicCommentsService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(path = "/comments")
@Validated
public class PublicCommentsController {
    private final PublicCommentsService commentsService;

    @Autowired
    public PublicCommentsController(PublicCommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<CommentDto>> getCommentsForEvent(@PathVariable @PositiveOrZero Long eventId,
                                                                @RequestParam(defaultValue = "0") Integer from,
                                                                @RequestParam(defaultValue = "10") Integer size) {
        List<CommentDto> result = commentsService.getCommentsForEvent(eventId, from, size);
        log.info("Запрос всех комментариев для события с id = {}.", eventId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{comId}")
    public ResponseEntity<CommentDto> getCommentsById(@PathVariable @PositiveOrZero Long comId) {
        CommentDto result = commentsService.getCommentsById(comId);
        log.info("Запрос комментария с id = {}", comId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
