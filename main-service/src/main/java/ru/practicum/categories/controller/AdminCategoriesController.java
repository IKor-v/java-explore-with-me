package ru.practicum.categories.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.service.AdminCategoriesService;
import ru.practicum.groupvalid.CreateInfo;
import ru.practicum.groupvalid.UpdateInfo;

@Slf4j
@Controller
@RequestMapping(path = "/admin/categories")
public class AdminCategoriesController {

    private final AdminCategoriesService adminCategoriesService;

    @Autowired
    public AdminCategoriesController(AdminCategoriesService adminCategoriesService) {
        this.adminCategoriesService = adminCategoriesService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> postNewCategory(@RequestBody @Validated(CreateInfo.class) CategoryDto categoryDto) {
        CategoryDto result = adminCategoriesService.postNewCategory(categoryDto);
        log.info("Созданна новая категория: {}", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> patchCategory(@PathVariable Long catId, @Validated(UpdateInfo.class) @RequestBody CategoryDto categoryDto) {
        CategoryDto result = adminCategoriesService.patchCategory(catId, categoryDto);
        log.info("Изменена категория: {}", result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Object> delCategories(@PathVariable Long catId) {
        adminCategoriesService.delCategory(catId);
        log.info("Удалена категория с id = {}", catId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
