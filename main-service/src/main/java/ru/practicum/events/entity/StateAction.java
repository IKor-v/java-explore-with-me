package ru.practicum.events.entity;

/**
 * Enum для статусов будущих действий с событием
 */
public enum StateAction {
    /**
     * Для Админа: Публикация события
     */
    PUBLISH_EVENT,
    /**
     * Для Админа: Отмена события
     */
    REJECT_EVENT,
    /**
     * Для Пользователя: Отправить на проверку
     */
    SEND_TO_REVIEW,
    /**
     * Для Пользователя: Отозвать
     */
    CANCEL_REVIEW
}
