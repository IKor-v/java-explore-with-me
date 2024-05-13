package ru.practicum.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.entity.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.events.dto.EventDtoFull;
import ru.practicum.events.dto.EventDtoIn;
import ru.practicum.events.dto.EventMapper;
import ru.practicum.events.dto.LocationMapper;
import ru.practicum.events.entity.Event;
import ru.practicum.events.entity.LocationEvent;
import ru.practicum.events.entity.StateAction;
import ru.practicum.events.entity.StateEvent;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.events.repository.LocationRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.stats.StatsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AdminEventsServiceImpl implements AdminEventsService {
    private final EventsRepository eventsRepository;
    private final CategoriesRepository categoriesRepository;
    private final LocationRepository locationRepository;
    private final StatsService statsService;

    @Autowired
    public AdminEventsServiceImpl(EventsRepository eventsRepository, CategoriesRepository categoriesRepository, LocationRepository locationRepository, StatsService statsService) {
        this.eventsRepository = eventsRepository;
        this.categoriesRepository = categoriesRepository;
        this.locationRepository = locationRepository;
        this.statsService = statsService;
    }

    @Override
    @Transactional
    public EventDtoFull patchEvent(Long eventId, EventDtoIn eventDtoIn) {
        if (eventDtoIn == null) {
            throw new ValidationException("Передано пустое тело события.");
        }
        Event oldEvent = eventsRepository.getById(eventId);

        if (oldEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConflictException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации.");
        }
        if (!oldEvent.getState().equals(StateEvent.PENDING)) {
            throw new ConflictException("Событие можно публиковать/отменять только в статусе ожидания.");
        }

        Category category;
        LocationEvent location;
        if ((eventDtoIn.getCategory() == null) || (oldEvent.getCategory().getId().equals(eventDtoIn.getCategory()))) {
            category = oldEvent.getCategory();
        } else {
            category = categoriesRepository.findById(eventDtoIn.getCategory()).orElseThrow(() -> new NotFoundException("Не найдена категория с id = " + eventDtoIn.getCategory()));
        }
        if ((eventDtoIn.getLocation() == null)
                || ((eventDtoIn.getLocation().getLon().equals(oldEvent.getLocation().getLon()))
                && (eventDtoIn.getLocation().getLat().equals(oldEvent.getLocation().getLat())))) {
            location = oldEvent.getLocation();
        } else {
            location = locationRepository.save(LocationMapper.toLocation(eventDtoIn.getLocation()));
        }

        Event newEvent = EventMapper.toUpdateEvent(oldEvent, eventDtoIn, category, location);
        if (eventDtoIn.getStateAction() != null) {
            if (eventDtoIn.getStateAction().equals(StateAction.PUBLISH_EVENT)) {
                newEvent.setState(StateEvent.PUBLISHED);
                newEvent.setPublishedOn(LocalDateTime.now());
            } else if (eventDtoIn.getStateAction().equals(StateAction.REJECT_EVENT)) {
                newEvent.setState(StateEvent.CANCELED);
            } else {
                throw new ValidationException("Получены некорректный данные для смены статуса.");
            }
        }

        return EventMapper.toEventDtoFull(eventsRepository.save(newEvent));
    }

    @Override
    public List<EventDtoFull> getEvents(List<Long> users, List<StateEvent> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> result;
        if (states != null) {
            result = eventsRepository.getEventsForAdmin(users, states.stream().map(Enum::ordinal).collect(Collectors.toList()),
                    categories, rangeStart, rangeEnd, pageable);
        } else {
            result = eventsRepository.getEventsForAdmin(users, null,
                    categories, rangeStart, rangeEnd, pageable);
        }

        Map<Long, Long> views = statsService.getView(result);
        ;

        return result.stream()
                .map(EventMapper::toEventDtoFull)
                .map(a -> {
                    a.setViews(views.getOrDefault(a.getId(), 0L));
                    return a;
                })
                .collect(Collectors.toList());
    }
}
