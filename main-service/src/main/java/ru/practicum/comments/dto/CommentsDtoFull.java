package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.dto.EventDto;
import ru.practicum.users.dto.UserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentsDtoFull {
    private Long id;
    private String text;
    private String createdOn;
    private UserDto creator;
    private EventDto event;
}
