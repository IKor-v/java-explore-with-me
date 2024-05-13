package ru.practicum.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.categories.entity.Category;

public interface CategoriesRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);
}
