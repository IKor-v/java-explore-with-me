package ru.practicum.categories.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.PublicCategoriesService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
public class PublicCategoriesController {
    private final PublicCategoriesService categoriesService;

    public PublicCategoriesController(PublicCategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories(@RequestParam(defaultValue = "0") Integer from,
                                                           @RequestParam(defaultValue = "10") Integer size) {
        List<CategoryDto> result = categoriesService.getCategories(from, size);
        log.info("Запрос всех категорий.");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long catId) {
        CategoryDto result = categoriesService.getCategoryById(catId);
        log.info("Запрос катеригои с id = {}", catId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
