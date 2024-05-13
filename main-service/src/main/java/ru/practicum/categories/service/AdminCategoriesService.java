package ru.practicum.categories.service;

import ru.practicum.categories.dto.CategoryDto;

public interface AdminCategoriesService {
    void delCategory(Long catId);

    CategoryDto patchCategory(Long catId, CategoryDto categoryDto);

    CategoryDto postNewCategory(CategoryDto categoryDto);
}
