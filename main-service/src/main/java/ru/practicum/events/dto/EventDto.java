package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.users.dto.UserDto;

/**
 * Сокращенный класс dto для события
 *
 * @see ru.practicum.events.entity.Event
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    private String eventDate;
    private UserDto initiator;
    private Boolean paid;
    private Long views;
}
