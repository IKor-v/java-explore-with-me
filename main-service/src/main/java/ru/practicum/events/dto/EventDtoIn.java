package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.entity.StateAction;
import ru.practicum.groupvalid.CreateInfo;
import ru.practicum.groupvalid.UpdateInfo;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDtoIn {
    @NotBlank(groups = {CreateInfo.class})
    @Size(min = 3, max = 120, groups = {CreateInfo.class, UpdateInfo.class})
    private String title;
    @NotBlank(groups = {CreateInfo.class})
    @Size(min = 20, max = 2000, groups = {CreateInfo.class, UpdateInfo.class})
    private String annotation;
    @NotNull(groups = {CreateInfo.class})
    @Positive(groups = {CreateInfo.class, UpdateInfo.class})
    private Long category;
    @NotBlank(groups = {CreateInfo.class})
    @Size(min = 20, max = 7000, groups = {CreateInfo.class, UpdateInfo.class})
    private String description;
    @NotNull(groups = {CreateInfo.class})
    @Future(groups = {CreateInfo.class, UpdateInfo.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private Boolean paid;
    @NotNull(groups = {CreateInfo.class})
    private LocationDto location;
    @PositiveOrZero(groups = {CreateInfo.class, UpdateInfo.class})
    private Integer participantLimit;
    private Boolean requestModeration;
    @Null(groups = {CreateInfo.class})
    private StateAction stateAction;
}
