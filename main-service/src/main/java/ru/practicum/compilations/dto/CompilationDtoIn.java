package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.groupvalid.CreateInfo;
import ru.practicum.groupvalid.UpdateInfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompilationDtoIn {

    @NotBlank(groups = {CreateInfo.class})
    @Size(min = 1, max = 50, groups = {CreateInfo.class, UpdateInfo.class})
    private String title;
    private Boolean pinned;
    private Set<Long> events;
}
