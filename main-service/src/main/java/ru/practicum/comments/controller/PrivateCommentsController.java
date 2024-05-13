package ru.practicum.comments.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentDtoIn;
import ru.practicum.comments.service.PrivateCommentsService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(path = "/comments/user/{userId}")
public class PrivateCommentsController {
    private final PrivateCommentsService commentsService;

    @Autowired
    public PrivateCommentsController(PrivateCommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<CommentDto> postComment(@PathVariable @PositiveOrZero Long userId,
                                                  @PathVariable @PositiveOrZero Long eventId,
                                                  @RequestBody @Valid CommentDtoIn commentDtoIn) {
        CommentDto commentDto = commentsService.postComment(userId, eventId, commentDtoIn);
        log.info("Пользователь {} добавляет комментарий для события с id = {eventId}", userId, eventId);
        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }

    @PatchMapping("/{comId}")
    public ResponseEntity<CommentDto> patchComment(@PathVariable @PositiveOrZero Long userId,
                                                   @PathVariable @PositiveOrZero Long comId,
                                                   @RequestBody @Valid CommentDtoIn commentDtoIn) {
        CommentDto commentDto = commentsService.patchComment(userId, comId, commentDtoIn);
        log.info("Пользователь {} изменяет свой комментарий для события с id = {}", userId, comId);
        return ResponseEntity.status(HttpStatus.OK).body(commentDto);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getAllMyComments(@PathVariable @PositiveOrZero Long userId,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                             @RequestParam(defaultValue = "0") Integer from,
                                                             @RequestParam(defaultValue = "10") Integer size) {
        List<CommentDto> commentDto = commentsService.getAllMyComments(userId, start, end, from, size);
        log.info("Пользователь {} запрашивает все свои комментарии.", userId);
        return ResponseEntity.status(HttpStatus.OK).body(commentDto);
    }

    @DeleteMapping("/{comId}")
    public ResponseEntity<Object> delComment(@PathVariable @PositiveOrZero Long userId,
                                             @PathVariable @PositiveOrZero Long comId) {
        commentsService.delCommentById(userId, comId);
        log.info("Пользователь {} удаляет свой комментарий с id = {}", userId, comId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
