package com.hikmetcakir.coreapi.controller;

import com.hikmetcakir.coreapi.dto.category.*;
import com.hikmetcakir.coreapi.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategorySaveResponse save(@Valid @RequestBody CategorySaveRequest request) {
        String id = categoryService.save(request);
        return CategorySaveResponse.builder().id(id).build();
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") String id, @Valid @RequestBody CategoryUpdateRequest request) {
        categoryService.update(id, request);
    }

    @GetMapping("/hierarchy")
    public List<CategoryHierarchy> queryCategoryHierarchy(@RequestParam(defaultValue = "1") List<Integer> levels) {
        return categoryService.queryCategoryHierarchy(levels);
    }

    @GetMapping
    public List<Category> query() {
        return categoryService.query();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") String id) {
        categoryService.delete(id);
    }
}
