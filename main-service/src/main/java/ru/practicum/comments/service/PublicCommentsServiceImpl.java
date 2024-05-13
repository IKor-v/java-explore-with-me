package ru.practicum.comments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentMapper;
import ru.practicum.comments.entity.Comment;
import ru.practicum.comments.repository.CommentsRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicCommentsServiceImpl implements PublicCommentsService {
    private final CommentsRepository commentsRepository;

    @Autowired
    public PublicCommentsServiceImpl(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }

    @Override
    public List<CommentDto> getCommentsForEvent(Long eventId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Comment> comments = commentsRepository.findByEventId(eventId, pageable);

        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentsById(Long comId) {
        return CommentMapper.toCommentDto(commentsRepository.findById(comId).orElseThrow(() -> new NotFoundException("Не найдено комментария с id = " + comId)));
    }
}
