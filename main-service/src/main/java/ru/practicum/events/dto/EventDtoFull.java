package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.events.entity.StateEvent;
import ru.practicum.users.dto.UserDto;

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

    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;

    private UserDto initiator;

    private Boolean paid;
    private Long views;

    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String createdOn;
    //@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String publishedOn;
    private String description;

    private LocationDto location;  //широта и долгота места
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    private StateEvent state;


}
