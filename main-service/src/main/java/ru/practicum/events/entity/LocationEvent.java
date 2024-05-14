package ru.practicum.events.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Класс сущности локации. Содержит информащию по широте и долготе места проведения события.
 */
@Entity
@Table(name = "locations", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationEvent {
    /**
     * Id номер локации
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Георграфическая широта локации.
     */
    @Column(name = "lat", nullable = false)
    private Float lat;
    /**
     * Георграфическоая долгота локации.
     */
    @Column(name = "lon", nullable = false)
    private Float lon;
}
