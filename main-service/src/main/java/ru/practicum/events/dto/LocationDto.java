package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Класс локации события, содержит широту и долготу места
 *
 * @see ru.practicum.events.entity.LocationEvent
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {
    /**
     * Георграфическая широта локации.
     */
    @NotNull
    private Float lat;
    /**
     * Георграфическая долгота локации.
     */
    @NotNull
    private Float lon;
}
