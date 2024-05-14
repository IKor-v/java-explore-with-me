package ru.practicum.events.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.categories.entity.Category;
import ru.practicum.users.entity.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "events", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
/**
 * Сущность события
 */
public class Event {
    /**
     * Поле id события
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Поле заголовка события
     */
    @Column(name = "title", nullable = false)
    private String title;
    /**
     * Поле кратного описания события
     */
    @Column(name = "annotation", nullable = false)
    private String annotation;
    /**
     * Поле колличества подтвержденных запросов на участие в событии
     */
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    /**
     * Поле даты-времени проведения события, конечный вид соответсвует паттерну "yyyy-MM-dd HH:mm:ss"
     */
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    /**
     * Поле статуса платности/бесплатности события
     */
    @Column(name = "paid")
    private Boolean paid;
    /**
     * Поле категории события. Хранит категорию
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Category category;
    /**
     * Поле создателя события. Хранит пользователя
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User initiator;

    /**
     * Поле даты-времени создания события, конечный вид соответсвует паттерну "yyyy-MM-dd HH:mm:ss"
     */
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;
    /**
     * Поле даты-времени публикации события, которое может быть null для неопубликованного события.
     * Конечный вид соответсвует паттерну "yyyy-MM-dd HH:mm:ss"
     */
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    /**
     * Поле подробного описания события.
     */
    @Column(name = "description")
    private String description;
    /**
     * Поле локации события. Содержит широту, долготу и id этой локации
     *
     * @see LocationEvent
     */
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private LocationEvent location;
    /**
     * Поле ограничения количества участников. По умолчанию равно 0, т.е. не ограничено.
     */
    @Column(name = "participant_limit")
    private Integer participantLimit = 0;
    /**
     * Поле статуса необходимости модерации запросов. По умалчанию - true, т.е. модерация требуется
     */
    @Column(name = "request_moderation")
    private Boolean requestModeration = true;
    /**
     * Поле актуального статуса события.
     *
     * @see StateEvent
     */
    @Column(name = "state", nullable = false)
    private StateEvent state;

}
