package ru.practicum.comments.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.comments.entity.Comment;
import ru.practicum.events.dto.EventMapper;
import ru.practicum.events.entity.Event;
import ru.practicum.users.dto.UserMapper;
import ru.practicum.users.entity.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .text(comment.getText())
                .createdOn(comment.getCreatedOn().format(EventMapper.formatter))
                .creator(UserMapper.toUserDto(comment.getCreator()))
                .build();
    }

    public Comment toComment(CommentDtoIn commentDtoIn, User user, Event event) {
        return Comment.builder()
                .text(commentDtoIn.getText())
                .createdOn(LocalDateTime.now())
                .creator(user)
                .event(event)
                .build();
    }

    public CommentsDtoFull toCommentDtoFull(Comment comment) {
        return CommentsDtoFull.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdOn(comment.getCreatedOn().format(EventMapper.formatter))
                .creator(UserMapper.toUserDto(comment.getCreator()))
                .event(EventMapper.toEventDto(comment.getEvent()))
                .build();
    }
}
