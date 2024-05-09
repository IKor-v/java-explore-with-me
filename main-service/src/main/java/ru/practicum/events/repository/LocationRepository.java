package ru.practicum.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.events.entity.LocationEvent;

public interface LocationRepository extends JpaRepository<LocationEvent, Long> {
}
