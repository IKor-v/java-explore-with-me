package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.requests.entity.Request;

import java.util.List;
import java.util.Set;

public interface RequestsRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(Long userId);

    Request findByEventIdAndRequesterId(Long eventId, Long userId);

    List<Request> findByIdIn(Set<Long> requestIds);

    List<Request> findByEventId(Long eventId);
}
