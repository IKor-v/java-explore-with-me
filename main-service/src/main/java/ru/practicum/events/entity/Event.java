package ru.practicum.events.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "annotation")
    private String annotation;
    @Column(name = "confirmed_requests")
    private Integer confirmedRequests;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "paid")
    private Boolean paid;
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    private User initiator;

    @Column(name = "created_on")
    private LocalDateTime createdOn;  //"yyyy-MM-dd HH:mm:ss"
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "description")
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    private LocationEvent location;  //широта и долгота места
    @Column(name = "participant_limit")
    private Integer participantLimit = 0;
    @Column(name = "request_moderation")
    private Boolean requestModeration = true;
    @Column(name = "state")
    private StateEvent state;

}
