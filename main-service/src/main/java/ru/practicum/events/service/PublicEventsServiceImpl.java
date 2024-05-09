package ru.practicum.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventDtoFull;
import ru.practicum.events.dto.EventMapper;
import ru.practicum.events.entity.AvailableSort;
import ru.practicum.events.entity.Event;
import ru.practicum.events.entity.StateEvent;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.events.repository.LocationRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.stats.StatsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PublicEventsServiceImpl implements PublicEventsService {
    private final EventsRepository eventsRepository;
    private final LocationRepository locationRepository;
    private final StatsService statsService;

    @Autowired
    public PublicEventsServiceImpl(EventsRepository eventsRepository, LocationRepository locationRepository, StatsService statsService) {
        this.eventsRepository = eventsRepository;
        this.locationRepository = locationRepository;
        this.statsService = statsService;
    }

    @Override
    public List<EventDto> getEvents(String text, List<Long> categories, Boolean paid,
                                    LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                    Boolean onlyAvailable, AvailableSort sort, Integer from, Integer size, HttpServletRequest request) {

        if ((rangeStart != null) && (rangeEnd != null)) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new RuntimeException("Дата и время начала промежутка не может быть позже даты и времени его окончания.");
            }
        }
        if ((rangeStart == null) && (rangeEnd == null)) {
            rangeStart = LocalDateTime.now();
        }

        Pageable pageable = PageRequest.of(from / size, size);

        List<Event> events;
        if ((sort != null) && (sort.equals(AvailableSort.EVENT_DATE))) {
            events = eventsRepository.getByParamSortByDate(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                    StateEvent.PUBLISHED.ordinal(), pageable);
        } else {
            events = eventsRepository.getByParam(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                    StateEvent.PUBLISHED.ordinal(), pageable);
        }

        List<EventDto> result;
        try {
            Map<Long, Long> views = statsService.getView(events);

            result = events.stream()
                    .map(EventMapper::toEventDto)
                    .map(a -> {
                        a.setViews(views.getOrDefault(a.getId(), 0L));
                        return a;
                    })
                    .peek(a -> {
                        statsService.addHit(request, a.getId());
                    })
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка подключения к серсису статистики");
        }
        return result;
    }


    @Override
    public EventDtoFull getEventById(Long id, HttpServletRequest request) {
        Event event = eventsRepository.findByIdAndState(id, StateEvent.PUBLISHED);
        if (event == null) {
            throw new NotFoundException("Среди опубликованных событий не найдено события с id = " + id);
        }

        Long view = 0L;
        try {
            Map<Long, Long> views = statsService.getView(List.of(event));

            if ((views != null) && (!views.isEmpty())) {
                view = views.get(id);
            }
            statsService.addHit(request);
        } catch (RuntimeException e) {
            throw new RuntimeException("Ошибка подключения к серсису статистики");
        }

        EventDtoFull result = EventMapper.toAdminEventDto(event);
        if ((view != null) && (view >= 0)) {
            result.setViews(view);
        }
        return result;

    }
}
