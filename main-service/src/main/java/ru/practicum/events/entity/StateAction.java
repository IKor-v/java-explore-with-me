package ru.practicum.events.entity;

public enum StateAction {
    PUBLISH_EVENT,      // опубликовать
    REJECT_EVENT,       // отменить
    SEND_TO_REVIEW,     // Отправить на проверку
    CANCEL_REVIEW       // Отозвать
}
