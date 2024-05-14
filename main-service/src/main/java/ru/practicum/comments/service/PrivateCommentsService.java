package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentDtoIn;

import java.time.LocalDateTime;
import java.util.List;

public interface PrivateCommentsService {
    CommentDto postComment(Long userId, Long eventId, CommentDtoIn commentDtoIn);

    CommentDto patchComment(Long userId, Long comId, CommentDtoIn commentDtoIn);

    List<CommentDto> getAllMyComments(Long userId, LocalDateTime start, LocalDateTime end, Integer from, Integer size);

    void delCommentById(Long userId, Long comId);
}
