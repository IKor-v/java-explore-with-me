package ru.practicum.events.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comments.dto.CommentDtoCount;
import ru.practicum.comments.dto.CommentMapper;
import ru.practicum.comments.entity.Comment;
import ru.practicum.comments.repository.CommentsRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PublicEventsServiceImpl implements PublicEventsService {
    private final EventsRepository eventsRepository;
    private final LocationRepository locationRepository;
    private final StatsService statsService;
    private final CommentsRepository commentsRepository;

    @Autowired
    public PublicEventsServiceImpl(EventsRepository eventsRepository, LocationRepository locationRepository, StatsService statsService, CommentsRepository commentsRepository) {
        this.eventsRepository = eventsRepository;
        this.locationRepository = locationRepository;
        this.statsService = statsService;
        this.commentsRepository = commentsRepository;
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
        List<CommentDtoCount> commentDtoCount = commentsRepository.countGroupByEventId();
        try {
            statsService.addHit(request);
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
                    .map(a -> {
                        List<Long> countComments = new ArrayList<>();
                        for (CommentDtoCount count : commentDtoCount) {
                            if (a.getId().equals(count.getEventId())) {
                                countComments.add(count.getCountComments());
                            }
                        }
                        a.setComments(countComments);
                        return a;
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

        EventDtoFull result = EventMapper.toEventDtoFull(event);
        if ((view != null) && (view >= 0)) {
            result.setViews(view);
        }

        List<Comment> comments = commentsRepository.findByEventId(result.getId());
        if (comments != null) {
            result.setComments(comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
        }
        return result;

    }
}
