package ru.practicum.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.entity.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.events.dto.ChangeRequestsListDto;
import ru.practicum.events.dto.EventDto;
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
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.dto.RequestMapper;
import ru.practicum.requests.entity.Request;
import ru.practicum.requests.repository.RequestsRepository;
import ru.practicum.users.entity.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PrivateEventsServiceImpl implements PrivateEventsService {
    private final EventsRepository eventsRepository;
    private final RequestsRepository requestsRepository;
    private final UserRepository userRepository;
    private final CategoriesRepository categoriesRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public PrivateEventsServiceImpl(EventsRepository eventsRepository, RequestsRepository requestsRepository, UserRepository userRepository, CategoriesRepository categoriesRepository, LocationRepository locationRepository) {
        this.eventsRepository = eventsRepository;
        this.requestsRepository = requestsRepository;
        this.userRepository = userRepository;
        this.categoriesRepository = categoriesRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<EventDto> getEventsForUser(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = eventsRepository.findAll(pageable).toList();
        return events.stream()
                .map(EventMapper::toEventDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDtoFull getEventForUserById(Long userId, Long eventId) {
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с id = " + eventId));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("У пользователя с id = " + userId + " не обнаруженно события с id = " + eventId);
        }
        return EventMapper.toAdminEventDto(event);
    }

    @Override
    @Transactional
    public EventDtoFull postNewEvent(Long userId, EventDtoIn eventDtoIn) {
        validation(eventDtoIn);

        if (eventDtoIn.getParticipantLimit() == null) {
            eventDtoIn.setParticipantLimit(0);
        }
        if (eventDtoIn.getRequestModeration() == null) {
            eventDtoIn.setRequestModeration(true);
        }
        if (eventDtoIn.getPaid() == null) {
            eventDtoIn.setPaid(false);
        }

        Category category = categoriesRepository.findById(eventDtoIn.getCategory()).orElseThrow(() -> new NotFoundException("Не найдена категория с id = " + eventDtoIn.getCategory()));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с id = " + userId));
        LocationEvent location = locationRepository.save(LocationMapper.toLocation(eventDtoIn.getLocation()));
        Event event = EventMapper.toEvent(eventDtoIn, category, user);
        event.setLocation(location);
        return EventMapper.toAdminEventDto(eventsRepository.save(event));
    }

    @Override
    @Transactional
    public EventDtoFull patchEvent(Long userId, Long eventId, EventDtoIn eventDtoIn) {
        validation(eventDtoIn);
        Event oldEvent = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не найдено событие с id = " + eventId));
        if (oldEvent.getState().equals(StateEvent.PUBLISHED)) {
            throw new ConflictException("Нельзя изменять опубликованные события.");
        }
        if (oldEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Нельзя изменять события, до которых осталось менее 2 часов.");
        }
        if (!oldEvent.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Нельзя менять чужие события.");
        }

        Category category;
        LocationEvent location;

        if ((eventDtoIn.getCategory() == null) || (oldEvent.getCategory().equals(eventDtoIn.getCategory()))) {
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
        Event event = EventMapper.toUpdateEvent(oldEvent, eventDtoIn, category, location);
        if (eventDtoIn.getStateAction() != null) {
            if (eventDtoIn.getStateAction().equals(StateAction.SEND_TO_REVIEW)) {
                event.setState(StateEvent.PENDING);
                event.setPublishedOn(LocalDateTime.now());
            } else if (eventDtoIn.getStateAction().equals(StateAction.CANCEL_REVIEW)) {
                event.setState(StateEvent.CANCELED);
            } else {
                throw new ValidationException("Получены некорректный данные для смены статуса.");
            }
        }

        return EventMapper.toAdminEventDto(eventsRepository.save(event));
    }


    @Override
    public List<RequestDto> getRequestsForEvent(Long userId, Long eventId) {
        List<Request> requests = requestsRepository.findByEventId(eventId);
        if ((requests != null) && (requests.size() > 0) && (!requests.get(0).getEvent().getInitiator().getId().equals(userId))) {
            throw new ConflictException("Нельзя просматривать запросы на участие чужого события.");
        }
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<RequestDto> changeResultStatus(ChangeRequestsListDto requestsList, Long userId, Long eventId) {
        List<Request> requests = requestsRepository.findByIdIn(requestsList.getRequestIds());
        List<Request> result = new ArrayList<>();
        if ((requests == null) || (requests.size() < requestsList.getRequestIds().size())) {
            throw new NotFoundException("Найдено запросов меньше, чем было указано.");
        }

        int countConfirmRequest = 0;
        int limitRequests = 0;
        Event event;
        if (requests.size() > 0) {
            event = requests.get(0).getEvent();
            countConfirmRequest = event.getConfirmedRequests();
            limitRequests = event.getParticipantLimit();
        } else {
            return List.of();
        }
        for (Request request : requests) {
            if ((limitRequests != 0) && (countConfirmRequest >= limitRequests)) {
                throw new ConflictException("Количество мест ограничено, подтвердить указанные запросы не получится.");
            }
            request.setStatus(requestsList.getStatus());
            result.add(request);
            countConfirmRequest++;
        }
        if (!result.isEmpty()) {
            event.setConfirmedRequests(countConfirmRequest);
            eventsRepository.save(event);
        }
        return requestsRepository.saveAll(requests).stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    private void validation(EventDtoIn eventDtoIn) {
        if (eventDtoIn == null) {
            throw new ValidationException("Передано пустое тело события.");
        }
    }
}
