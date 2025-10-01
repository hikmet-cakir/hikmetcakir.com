package com.hikmetcakir.coreapi.controller;

import com.hikmetcakir.coreapi.dto.category.CategorySaveRequest;
import com.hikmetcakir.coreapi.dto.category.CategorySaveResponse;
import com.hikmetcakir.coreapi.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public CategorySaveResponse save(@RequestBody CategorySaveRequest request) {
        String id = categoryService.save(request);
        return CategorySaveResponse.builder().id(id).build();
    }
}
