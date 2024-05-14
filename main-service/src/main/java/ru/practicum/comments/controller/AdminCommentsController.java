package ru.practicum.comments.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.comments.dto.CommentsDtoFull;
import ru.practicum.comments.service.AdminCommentsService;

import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(path = "admin/comments")
@Validated
public class AdminCommentsController {
    private final AdminCommentsService adminCommentsService;

    @Autowired
    public AdminCommentsController(AdminCommentsService adminCommentsService) {
        this.adminCommentsService = adminCommentsService;
    }

    @GetMapping("/{comId}")
    public ResponseEntity<CommentsDtoFull> getCommentForAdmin(@PathVariable @PositiveOrZero Long comId) {
        CommentsDtoFull result = adminCommentsService.getComments(comId);
        log.info("Админ запрашивает комментарий {}.", comId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping
    public ResponseEntity<List<CommentsDtoFull>> getAllCommentsForAdmin(@RequestParam(required = false) List<Long> eventId,
                                                                        @RequestParam(required = false) List<Long> userId,
                                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                                        @RequestParam(defaultValue = "0") Integer from,
                                                                        @RequestParam(defaultValue = "10") Integer size) {
        List<CommentsDtoFull> result = adminCommentsService.getAllComments(eventId, userId, start, end, from, size);
        log.info("Админ запрашивает все комментарии.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{comId}")
    public ResponseEntity<Object> delCommentForAdmin(@PathVariable @PositiveOrZero Long comId) {
        adminCommentsService.delComment(comId);
        log.info("Админ удаляет комментарий с id = {}", comId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
