package com.hikmetcakir.coreapi.controller;

import com.hikmetcakir.coreapi.dto.category.Category;
import com.hikmetcakir.coreapi.dto.category.CategoryHierarchy;
import com.hikmetcakir.coreapi.dto.category.CategorySaveRequest;
import com.hikmetcakir.coreapi.dto.category.CategorySaveResponse;
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

    @GetMapping("/hierarchy")
    public List<CategoryHierarchy> queryCategoryHierarchy(@RequestParam(defaultValue = "1") List<Integer> levels) {
        return categoryService.queryCategoryHierarchy(levels);
    }

    @GetMapping
    public List<Category> query() {
        return categoryService.query();
    }
}
