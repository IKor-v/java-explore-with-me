package ru.practicum.categories.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.CategoryMapper;
import ru.practicum.categories.entity.Category;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.events.entity.Event;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import javax.validation.ValidationException;

@Service
@Transactional(readOnly = true)
public class AdminCategoriesServiceImpl implements AdminCategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final EventsRepository eventsRepository;

    @Autowired
    public AdminCategoriesServiceImpl(CategoriesRepository categoriesRepository, EventsRepository eventsRepository) {
        this.categoriesRepository = categoriesRepository;
        this.eventsRepository = eventsRepository;
    }

    @Override
    @Transactional
    public CategoryDto postNewCategory(CategoryDto categoryDto) {
        validation(categoryDto);
        try {
            return CategoryMapper.toCategoryDto(categoriesRepository.save(CategoryMapper.toCategory(categoryDto)));
        } catch (RuntimeException e) {
            throw new ConflictException("Ошибка при сохранении категории: попробуйте другое название.");
        }
    }

    @Override
    @Transactional
    public void delCategory(Long catId) {
        existEventByCategory(catId);
        categoriesRepository.deleteById(catId);

    }

    @Override
    @Transactional
    public CategoryDto patchCategory(Long catId, CategoryDto categoryDto) {
        validation(categoryDto);
        if (categoryDto.getId() != null) {
            if (!categoryDto.getId().equals(catId)) {
                throw new ValidationException("Указан неверный id в теле запроса.");
            }
        }
        Category category = categoriesRepository.findById(catId).orElseThrow(() -> new NotFoundException("Не найдено категории с id = " + catId));
        if ((categoryDto.getName() == null) || (categoryDto.getName().equals(category.getName()))) {
            return CategoryMapper.toCategoryDto(category);
        } else {
            isExistName(categoryDto.getName());
            category.setName(categoryDto.getName());
            return CategoryMapper.toCategoryDto(categoriesRepository.save(category));
        }

    }

    private void existEventByCategory(Long catId) {
        Event event = eventsRepository.findByCategoryId(catId);
        if (event != null) {
            throw new ConflictException("Ошибка при удалении категории: в этой категории существуют события.");
        }
    }

    private void isExistName(String name) {
        Category category = categoriesRepository.findByName(name);
        if (category != null) {
            throw new ConflictException("Ошибка при изменении подборки: попробуйте другое название.");
        }
    }

    private void validation(CategoryDto categoryDto) {
        if (categoryDto == null) {
            throw new ValidationException("Пустое тело категории недопустимо.");
        }
        if ((categoryDto.getName() == null) || (categoryDto.getName().isBlank())) {
            throw new ValidationException("Имя категории не может быть пустым");
        }
    }

}
