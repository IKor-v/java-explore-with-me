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


    List<Event> findByEventDateBefore(LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByEventDateAfter(LocalDateTime rangeStart, Pageable pageable);

    List<Event> findByEventDateBetween(LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByCategoryIdIn(List<Long> categories, Pageable pageable);

    List<Event> findByCategoryIdInAndEventDateBefore(List<Long> categories, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByCategoryIdInAndEventDateAfter(List<Long> categories, LocalDateTime rangeStart, Pageable pageable);

    List<Event> findByCategoryIdInAndEventDateBetween(List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByStateIn(List<StateEvent> states, Pageable pageable);

    List<Event> findByStateInAndEventDateBefore(List<StateEvent> states, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByStateInAndEventDateAfter(List<StateEvent> states, LocalDateTime rangeStart, Pageable pageable);

    List<Event> findByStateInAndEventDateBetween(List<StateEvent> states, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByStateInAndCategoryIdIn(List<StateEvent> states, List<Long> categories, Pageable pageable);

    List<Event> findByStateInAndCategoryIdInAndEventDateBefore(List<StateEvent> states, List<Long> categories, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByStateInAndCategoryIdInAndEventDateAfter(List<StateEvent> states, List<Long> categories, LocalDateTime rangeStart, Pageable pageable);

    List<Event> findByStateInAndCategoryIdInAndEventDateBetween(List<StateEvent> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByInitiatorIdIn(List<Long> users, Pageable pageable);

    List<Event> findByInitiatorIdInAndEventDateBefore(List<Long> users, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByInitiatorIdInAndEventDateAfter(List<Long> users, LocalDateTime rangeStart, Pageable pageable);


    List<Event> findByInitiatorIdInAndEventDateBetween(List<Long> users, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByInitiatorIdInAndCategoryIdIn(List<Long> users, List<Long> categories, Pageable pageable);

    List<Event> findByInitiatorIdInAndCategoryIdInAndEventDateBefore(List<Long> users, List<Long> categories, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByInitiatorIdInAndCategoryIdInAndEventDateAfter(List<Long> users, List<Long> categories, LocalDateTime rangeStart, Pageable pageable);

    List<Event> findByInitiatorIdInAndCategoryIdInAndEventDateBetween(List<Long> users, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByInitiatorIdInAndStateIn(List<Long> users, List<StateEvent> states, Pageable pageable);

    List<Event> findByInitiatorIdInAndStateInAndEventDateBefore(List<Long> users, List<StateEvent> states, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByInitiatorIdInAndStateInAndEventDateAfter(List<Long> users, List<StateEvent> states, LocalDateTime rangeStart, Pageable pageable);

    List<Event> findByInitiatorIdInAndStateInAndEventDateBetween(List<Long> users, List<StateEvent> states, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByInitiatorIdInAndStateInAndCategoryIdIn(List<Long> users, List<StateEvent> states, List<Long> categories, Pageable pageable);

    List<Event> findByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBefore(List<Long> users, List<StateEvent> states, List<Long> categories, LocalDateTime rangeEnd, Pageable pageable);

    List<Event> findByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfter(List<Long> users, List<StateEvent> states, List<Long> categories, LocalDateTime rangeStart, Pageable pageable);

    List<Event> findByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetween(List<Long> users, List<StateEvent> states, List<Long> categories, LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);


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
}
