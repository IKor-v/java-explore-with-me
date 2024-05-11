package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.events.entity.StateEvent;
import ru.practicum.users.dto.UserDto;

import java.time.LocalDateTime;

/**
 * Средний dto класс для события
 *
 * @see ru.practicum.events.entity.Event
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivateEventDto {

    private String title;

    private String annotation;

    private CategoryDto category;
    private Integer confirmedRequests;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private UserDto initiator;

    private Boolean paid;
    private Long views;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private String description;

    private LocationDto location;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    private StateEvent state;
}
