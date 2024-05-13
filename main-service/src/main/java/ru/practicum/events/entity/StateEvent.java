package ru.practicum.events.entity;

/**
 * Enum для актуально статуса события
 */
public enum StateEvent {
    /**
     * Событие в ожидании
     */
    PENDING,
    /**
     * Событие опубликовано
     */
    PUBLISHED,
    /**
     * Событие отменено
     */
    CANCELED
}
