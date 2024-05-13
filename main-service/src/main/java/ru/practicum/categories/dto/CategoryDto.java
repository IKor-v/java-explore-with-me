package ru.practicum.categories.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.groupvalid.CreateInfo;
import ru.practicum.groupvalid.UpdateInfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {
    @Null(groups = {UpdateInfo.class})
    private Long id;
    @NotBlank(groups = {CreateInfo.class})
    @Size(min = 1, max = 50, groups = {CreateInfo.class, UpdateInfo.class})
    private String name;
}
