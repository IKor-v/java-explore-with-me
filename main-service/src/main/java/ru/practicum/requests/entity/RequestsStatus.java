package ru.practicum.requests.entity;

/**
 * Список возможных статусов для запроса
 */
public enum RequestsStatus {
    /**
     * Запрос в ожидании
     */
    PENDING,
    /**
     * Запрос подтвержден
     */
    CONFIRMED,
    /**
     * Запрос отменен
     */
    CANCELED,
    /**
     * Запрос отозван
     */
    REJECTED
}
