package ru.practicum.events.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.events.entity.LocationEvent;

@UtilityClass
public class LocationMapper {
    public LocationDto toLocationDto(LocationEvent location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public LocationEvent toLocation(LocationDto location) {
        return LocationEvent.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
