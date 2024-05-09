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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AdminEventsServiceImpl implements AdminEventsService {
    private final EventsRepository eventsRepository;
    private final CategoriesRepository categoriesRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public AdminEventsServiceImpl(EventsRepository eventsRepository, CategoriesRepository categoriesRepository, LocationRepository locationRepository) {
        this.eventsRepository = eventsRepository;
        this.categoriesRepository = categoriesRepository;
        this.locationRepository = locationRepository;
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

        return EventMapper.toAdminEventDto(eventsRepository.save(newEvent));
    }

    @Override
    public List<EventDtoFull> getEvents(List<Long> users, List<StateEvent> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> result;
        int checkParam = getCheckParam(users, states, categories, rangeStart, rangeEnd);
        switch (checkParam) {
            case 0: {
                result = eventsRepository.findAll(pageable).toList();
                break;
            }
            case 1: {
                result = eventsRepository.findByEventDateBefore(rangeEnd, pageable);
                break;
            }
            case 10: {
                result = eventsRepository.findByEventDateAfter(rangeStart, pageable);
                break;
            }
            case 11: {
                result = eventsRepository.findByEventDateBetween(rangeStart, rangeEnd, pageable);
                break;
            }
            case 100: {
                result = eventsRepository.findByCategoryIdIn(categories, pageable);
                break;
            }
            case 101: {
                result = eventsRepository.findByCategoryIdInAndEventDateBefore(categories, rangeEnd, pageable);
                break;
            }
            case 110: {
                result = eventsRepository.findByCategoryIdInAndEventDateAfter(categories, rangeStart, pageable);
                break;
            }
            case 111: {
                result = eventsRepository.findByCategoryIdInAndEventDateBetween(categories, rangeStart, rangeEnd, pageable);
                break;
            }
            case 1000: {
                result = eventsRepository.findByStateIn(states, pageable);
                break;
            }
            case 1001: {
                result = eventsRepository.findByStateInAndEventDateBefore(states, rangeEnd, pageable);
                break;
            }
            case 1010: {
                result = eventsRepository.findByStateInAndEventDateAfter(states, rangeStart, pageable);
                break;
            }
            case 1011: {
                result = eventsRepository.findByStateInAndEventDateBetween(states, rangeStart, rangeEnd, pageable);
                break;
            }
            case 1100: {
                result = eventsRepository.findByStateInAndCategoryIdIn(states, categories, pageable);
                break;
            }
            case 1101: {
                result = eventsRepository.findByStateInAndCategoryIdInAndEventDateBefore(states, categories, rangeEnd, pageable);
                break;
            }
            case 1110: {
                result = eventsRepository.findByStateInAndCategoryIdInAndEventDateAfter(states, categories, rangeStart, pageable);
                break;
            }
            case 1111: {
                result = eventsRepository.findByStateInAndCategoryIdInAndEventDateBetween(states, categories, rangeStart, rangeEnd, pageable);
                break;
            }
            case 10000: {
                result = eventsRepository.findByInitiatorIdIn(users, pageable);
                break;
            }
            case 10001: {
                result = eventsRepository.findByInitiatorIdInAndEventDateBefore(users, rangeEnd, pageable);
                break;
            }
            case 10010: {
                result = eventsRepository.findByInitiatorIdInAndEventDateAfter(users, rangeStart, pageable);
                break;
            }
            case 10011: {
                result = eventsRepository.findByInitiatorIdInAndEventDateBetween(users, rangeStart, rangeEnd, pageable);
                break;
            }
            case 10100: {
                result = eventsRepository.findByInitiatorIdInAndCategoryIdIn(users, categories, pageable);
                break;
            }
            case 10101: {
                result = eventsRepository.findByInitiatorIdInAndCategoryIdInAndEventDateBefore(users, categories, rangeEnd, pageable);
                break;
            }
            case 10110: {
                result = eventsRepository.findByInitiatorIdInAndCategoryIdInAndEventDateAfter(users, categories, rangeStart, pageable);
                break;
            }
            case 10111: {
                result = eventsRepository.findByInitiatorIdInAndCategoryIdInAndEventDateBetween(users, categories, rangeStart, rangeEnd, pageable);
                break;
            }
            case 11000: {
                result = eventsRepository.findByInitiatorIdInAndStateIn(users, states, pageable);
                break;
            }
            case 11001: {
                result = eventsRepository.findByInitiatorIdInAndStateInAndEventDateBefore(users, states, rangeEnd, pageable);
                break;
            }
            case 11010: {
                result = eventsRepository.findByInitiatorIdInAndStateInAndEventDateAfter(users, states, rangeStart, pageable);
                break;
            }
            case 11011: {
                result = eventsRepository.findByInitiatorIdInAndStateInAndEventDateBetween(users, states, rangeStart, rangeEnd, pageable);
                break;
            }
            case 11100: {
                result = eventsRepository.findByInitiatorIdInAndStateInAndCategoryIdIn(users, states, categories, pageable);
                break;
            }
            case 11101: {
                result = eventsRepository.findByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBefore(users, states, categories, rangeEnd, pageable);
                break;
            }
            case 11110: {
                result = eventsRepository.findByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfter(users, states, categories, rangeStart, pageable);
                break;
            }
            case 11111: {
                result = eventsRepository.findByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetween(users, states, categories, rangeStart, rangeEnd, pageable);
                break;
            }
            default: {
                throw new RuntimeException("Ошибка обработки параметров запроса.");
            }
        }

        return result.stream()
                .map(EventMapper::toAdminEventDto)
                .collect(Collectors.toList());
    }

    private int getCheckParam(Object... o) {
        int result = 0;
        for (Object obj : o) {
            if (obj != null) {
                result++;
            }
            result = result * 10;
        }
        return result / 10;
    }
}
