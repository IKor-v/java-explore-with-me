package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.comments.dto.CommentDto;
import ru.practicum.events.entity.StateEvent;
import ru.practicum.users.dto.UserDto;

import java.util.List;

/**
 * Полный dto класс для события со всеми данными
 *
 * @see ru.practicum.events.entity.Event
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDtoFull {
    private Long id;

    private String title;

    private String annotation;

    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;

    private UserDto initiator;

    private Boolean paid;
    private Long views = 0L;
    private String createdOn;
    private String publishedOn;
    private String description;

    private LocationDto location;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    private StateEvent state;
    private List<CommentDto> comments;

}
