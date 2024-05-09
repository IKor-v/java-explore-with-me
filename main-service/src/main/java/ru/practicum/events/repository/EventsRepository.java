package ru.practicum.events.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.events.entity.Event;
import ru.practicum.events.entity.StateEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventsRepository extends JpaRepository<Event, Long> {
    List<Event> findByIdIn(Set<Long> events);

    Event findByIdAndState(Long id, StateEvent published);

    @Query("Select e " +
            "from Event as e " +
            "where ((?1 IS NULL) or (lower(e.annotation) like lower(concat('%', ?1, '%')) " +
            "or lower(e.description) like lower(concat('%',?1, '%')))) " +
            "and ((?2 IS NULL) or (e.category.id in ?2)) " +
            "and ((?3 IS NULL) or (e.paid = ?3)) " +
            "and ((?4 IS NULL) or (e.eventDate >= ?4)) " +
            "and ((?5 IS NULL) or (e.eventDate <= ?5)) " +
            "and ((?6 = false) or ((?6 = true) and (e.participantLimit = '0' or e.participantLimit > e.confirmedRequests))) " +
            "and ((?7 IS NULL) or (e.state = ?7)) " +
            "order by e.eventDate")
    List<Event> getByParamSortByDate(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Boolean onlyAvailable, int published, Pageable pageable);

    @Query("Select e " +
            "from Event as e " +
            "where ((?1 IS NULL) or (lower(e.annotation) like lower(concat('%', ?1, '%')) " +
            "or lower(e.description) like lower(concat('%',?1, '%')))) " +
            "and ((?2 IS NULL) or (e.category.id in ?2)) " +
            "and ((?3 IS NULL) or (e.paid = ?3)) " +
            "and ((?4 IS NULL) or (e.eventDate >= ?4)) " +
            "and ((?5 IS NULL) or (e.eventDate <= ?5)) " +
            "and ((?6 = false) or ((?6 = true) and (e.participantLimit = '0' or e.participantLimit > e.confirmedRequests))) " +
            "and ((?7 IS NULL) or (e.state = ?7)) " +
            "order by e.confirmedRequests")
    List<Event> getByParam(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                           LocalDateTime rangeEnd, Boolean onlyAvailable, int published, Pageable pageable);

    Event findByCategoryId(Long catId);

    @Query("Select e " +
            "from Event as e " +
            "where ((?1 IS NULL) or (e.initiator.id in ?1)) " +
            "and ((?2 IS NULL) or (e.state in ?2)) " +
            "and ((?3 IS NULL) or (e.category.id in ?3)) " +
            "and ((?4 IS NULL) or (e.eventDate >= ?4)) " +
            "and ((?5 IS NULL) or (e.eventDate <= ?5)) ")
        //"order by e.id")
    List<Event> getEventsForAdmin(List<Long> users, List<Integer> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
