package ru.practicum.requests.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.events.entity.Event;
import ru.practicum.users.entity.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "status", nullable = false)
    private RequestsStatus status = RequestsStatus.PENDING;
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private Event event;
    @ToString.Exclude
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    private User requester;
}
