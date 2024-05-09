package ru.practicum.events.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.categories.dto.CategoryMapper;
import ru.practicum.categories.entity.Category;
import ru.practicum.events.entity.Event;
import ru.practicum.events.entity.LocationEvent;
import ru.practicum.events.entity.StateEvent;
import ru.practicum.users.dto.UserMapper;
import ru.practicum.users.entity.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class EventMapper {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Event toEvent(EventDto eventDto) {
        return Event.builder()
                .id(eventDto.getId())
                .title(eventDto.getTitle())
                .annotation(eventDto.getAnnotation())
                .category(CategoryMapper.toCategory(eventDto.getCategory()))
                .confirmedRequests(eventDto.getConfirmedRequests())
                .eventDate(LocalDateTime.parse(eventDto.getEventDate(), formatter))
                .initiator(UserMapper.toUser(eventDto.getInitiator()))
                .paid(eventDto.getPaid())
                .build();
    }

    public EventDtoFull toAdminEventDto(Event event) {
        EventDtoFull result = EventDtoFull.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .paid(event.getPaid())
                .description(event.getDescription())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .initiator(UserMapper.toUserDto(event.getInitiator()))
                .build();


        if (event.getEventDate() != null) {
            result.setEventDate(event.getEventDate().format(formatter));
        }
        if (event.getCreatedOn() != null) {
            result.setCreatedOn(event.getCreatedOn().format(formatter));
        }
        if (event.getPublishedOn() != null) {
            result.setPublishedOn(event.getPublishedOn().format(formatter));
        }
        return result;
    }

    public EventDto toEventDto(Event event, Long views) {
        return EventDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(formatter))
                .initiator(UserMapper.toUserDto(event.getInitiator()))
                .paid(event.getPaid())
                .views(views)
                .build();
    }

    public EventDto toEventDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(formatter))
                .initiator(UserMapper.toUserDto(event.getInitiator()))
                .paid(event.getPaid())
                .views(0L)
                .build();
    }


    public PrivateEventDto toPrivateEventDto(Event event) {
        return PrivateEventDto.builder()
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .paid(event.getPaid())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .description(event.getDescription())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .location(LocationMapper.toLocationDto(event.getLocation()))
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .initiator(UserMapper.toUserDto(event.getInitiator()))
                .build();
    }

    public Event toEvent(EventDtoIn eventDtoIn, Category category, User initiator) {
        return Event.builder()
                .title(eventDtoIn.getTitle())
                .annotation(eventDtoIn.getAnnotation())
                .confirmedRequests(0)
                .eventDate(eventDtoIn.getEventDate())
                .paid(eventDtoIn.getPaid())
                .createdOn(LocalDateTime.now())
                .description(eventDtoIn.getDescription())
                .participantLimit(eventDtoIn.getParticipantLimit())
                .requestModeration(eventDtoIn.getRequestModeration())
                .state(StateEvent.PENDING)
                .location(LocationMapper.toLocation(eventDtoIn.getLocation()))
                .category(category)
                .initiator(initiator)
                .build();
    }

    public Event toUpdateEvent(Event event, EventDtoIn eventDtoIn, Category category, LocationEvent location) {
        if (eventDtoIn.getTitle() != null) {
            event.setTitle(eventDtoIn.getTitle());
        }
        if (eventDtoIn.getAnnotation() != null) {
            event.setAnnotation(eventDtoIn.getAnnotation());
        }
        if (eventDtoIn.getDescription() != null) {
            event.setDescription(eventDtoIn.getDescription());
        }
        if (eventDtoIn.getEventDate() != null) {
            event.setEventDate(eventDtoIn.getEventDate());
        }
        if (eventDtoIn.getPaid() != null) {
            event.setPaid(eventDtoIn.getPaid());
        }
        if (eventDtoIn.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDtoIn.getParticipantLimit());
        }
        if (eventDtoIn.getRequestModeration() != null) {
            event.setRequestModeration(eventDtoIn.getRequestModeration());
        }
        event.setCategory(category);
        event.setLocation(location);
        return event;
    }
}
