package ru.practicum.comments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentMapper;
import ru.practicum.comments.dto.CommentsDtoFull;
import ru.practicum.comments.entity.Comment;
import ru.practicum.comments.repository.CommentsRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminCommentsServiceImpl implements AdminCommentsService {
    private final CommentsRepository commentsRepository;

    @Autowired
    public AdminCommentsServiceImpl(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    @Override
    public CommentsDtoFull getComments(Long comId) {
        return CommentMapper.toCommentDtoFull(commentsRepository.findById(comId)
                .orElseThrow(() -> new NotFoundException("Не найдено комментария с id = " + comId)));
    }

    @Override
    public List<CommentsDtoFull> getAllComments(List<Long> eventId, List<Long> userId, LocalDateTime start, LocalDateTime end, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if ((start != null) && (end != null)) {
            if (start.isAfter(end)) {
                throw new ValidationException("Начало промежутка не может быть позже его конца.");
            }
        }
        List<Comment> comments = commentsRepository.getCommentsWithParam(eventId, userId, start, end, pageable);
        return comments.stream().map(CommentMapper::toCommentDtoFull).collect(Collectors.toList());
    }

    @Override
    public void delComment(Long comId) {
        commentsRepository.deleteById(comId);
    }
}
