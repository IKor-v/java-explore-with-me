package ru.practicum.comments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.comments.dto.CommentDtoIn;
import ru.practicum.comments.dto.CommentMapper;
import ru.practicum.comments.entity.Comment;
import ru.practicum.comments.repository.CommentsRepository;
import ru.practicum.events.entity.Event;
import ru.practicum.events.entity.StateEvent;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.users.entity.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrivateCommentsServiceImpl implements PrivateCommentsService {
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;

    @Autowired
    public PrivateCommentsServiceImpl(CommentsRepository commentsRepository, UserRepository userRepository, EventsRepository eventsRepository) {
        this.commentsRepository = commentsRepository;
        this.userRepository = userRepository;
        this.eventsRepository = eventsRepository;
    }

    @Override
    public CommentDto postComment(Long userId, Long eventId, CommentDtoIn commentDtoIn) {
        validation(commentDtoIn);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с id = " + userId));
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с id = " + eventId));
        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ConflictException("Нельзя комментировать события, которые не опубликованны.");
        }
        return CommentMapper.toCommentDto(commentsRepository.save(CommentMapper.toComment(commentDtoIn, user, event)));
    }

    @Override
    public CommentDto patchComment(Long userId, Long comId, CommentDtoIn commentDtoIn) {
        validation(commentDtoIn);
        Comment oldComment = commentsRepository.findById(comId).orElseThrow(() -> new NotFoundException("Не найден комментарий с id = " + comId));
        if (oldComment.getText().equals(commentDtoIn.getText())) {
            return CommentMapper.toCommentDto(oldComment);
        }
        oldComment.setText(commentDtoIn.getText());
        return CommentMapper.toCommentDto(commentsRepository.save(oldComment));
    }

    @Override
    public List<CommentDto> getAllMyComments(Long userId, LocalDateTime start, LocalDateTime end, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Comment> comments;
        if ((start != null) && (end == null)) {
            end = LocalDateTime.now().plusSeconds(1);
        }
        if (start != null) {
            if (start.isAfter(end)) {
                throw new ValidationException("Начало промежутка для отображения комментариев не может быть позже его конца.");
            }
            comments = commentsRepository.findByCreatorIdAndCreatedOnBetween(userId, start, end, pageable);
        } else {
            if (end != null) {
                comments = commentsRepository.findByCreatorIdAndCreatedOnBefore(userId, end, pageable);
            } else {
                comments = commentsRepository.findByCreatorId(userId, pageable);
            }
        }
        return comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
    }

    @Override
    public void delCommentById(Long userId, Long comId) {
        Comment comment = commentsRepository.findById(comId).orElseThrow(() -> new NotFoundException("Не найден комментарий с id = " + comId));
        if (!comment.getCreator().getId().equals(userId)) {
            throw new ConflictException("Нельзя удалять чужие комментарии.");
        }
        commentsRepository.deleteById(comId);
    }

    private void validation(CommentDtoIn commentDtoIn) {
        if (commentDtoIn == null) {
            throw new ValidationException("Передано пустое тело комментария.");
        }
        if ((commentDtoIn.getText() == null) || (commentDtoIn.getText().isBlank())) {
            throw new ValidationException("Текст комментария не может быть пустым.");
        }
    }
}
