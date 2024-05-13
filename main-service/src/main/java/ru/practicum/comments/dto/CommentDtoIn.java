package ru.practicum.comments.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDtoIn {
    @NotBlank(message = "Текст комментария не может быть пустым.")
    @Size(min = 1, max = 7000, message = "Текст комментария должен быть длиной от 1 до 7000 символов.")
    private String text;
}
