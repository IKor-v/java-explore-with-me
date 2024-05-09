package ru.practicum.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.entity.Event;
import ru.practicum.events.entity.StateEvent;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.requests.dto.RequestDto;
import ru.practicum.requests.dto.RequestMapper;
import ru.practicum.requests.entity.Request;
import ru.practicum.requests.entity.RequestsStatus;
import ru.practicum.requests.repository.RequestsRepository;
import ru.practicum.users.entity.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PrivateRequestsServiceImpl implements PrivateRequestsService {
    private final RequestsRepository repository;
    private final EventsRepository eventsRepository;
    private final UserRepository userRepository;

    @Autowired
    public PrivateRequestsServiceImpl(RequestsRepository repository, EventsRepository eventsRepository, UserRepository userRepository) {
        this.repository = repository;
        this.eventsRepository = eventsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<RequestDto> getRequests(Long userId) {
        List<Request> listRequests = repository.findByRequesterId(userId);
        return listRequests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDto postRequests(Long userId, Long eventId) {
        Event event = eventsRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Указаного события не существует."));
        Request request = repository.findByEventIdAndRequesterId(eventId, userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найден пользователь с id = " + userId));
        if (request == null) {
            request = new Request();
        } else {
            throw new ConflictException("Такой запрос уже существует.");
        }
        boolean checkUpdateEvent = false;
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Нельзя отправлять запрос на участие в своё событие.");
        }
        if (!event.getState().equals(StateEvent.PUBLISHED)) {
            throw new ConflictException("Нельзя отправлять запрос на участив в событие, которое не опубликованно.");
        }
        if ((event.getParticipantLimit() <= event.getConfirmedRequests()) && (!event.getParticipantLimit().equals(0))) {
            throw new ConflictException("Свободных мест не осталось, отправить запрос не получится.");
        }

        request.setEvent(event);
        request.setRequester(user);

        if (!event.getRequestModeration()) {
            request.setStatus(RequestsStatus.CONFIRMED);
            checkUpdateEvent = true;
        }
        request = repository.save(request);
        if (checkUpdateEvent) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventsRepository.save(event);
        }
        return RequestMapper.toRequestDto(request);
    }

    @Override
    @Transactional
    public RequestDto cancelRequests(Long userId, Long requestId) {
        Request request = repository.findById(requestId).orElseThrow(() -> new ValidationException("Такого запроса с id = " + requestId + " не найденно"));
        request.setStatus(RequestsStatus.CANCELED); //или REJECTED?
        return RequestMapper.toRequestDto(repository.save(request));
    }
}
