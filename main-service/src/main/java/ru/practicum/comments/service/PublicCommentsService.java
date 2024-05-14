package ru.practicum.comments.service;

import ru.practicum.comments.dto.CommentDto;

import java.util.List;

public interface PublicCommentsService {
    List<CommentDto> getCommentsForEvent(Long eventId, Integer from, Integer size);

    CommentDto getCommentsById(Long comId);
}
