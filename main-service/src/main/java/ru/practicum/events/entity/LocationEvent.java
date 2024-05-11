package ru.practicum.events.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
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
    @Column(name = "lat")
    private Float lat;
    /**
     * Георграфическоая долгота локации.
     */
    @Column(name = "lon")
    private Float lon;
}
