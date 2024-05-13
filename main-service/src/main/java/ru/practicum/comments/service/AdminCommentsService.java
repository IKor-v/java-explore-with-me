package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentsDtoFull;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminCommentsService {
    CommentsDtoFull getComments(Long comId);

    List<CommentsDtoFull> getAllComments(List<Long> eventId, List<Long> userId, LocalDateTime start, LocalDateTime end, Integer from, Integer size);

    void delComment(Long comId);
}
