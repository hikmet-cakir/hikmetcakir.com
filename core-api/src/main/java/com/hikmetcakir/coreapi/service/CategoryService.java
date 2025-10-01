package com.hikmetcakir.coreapi.service;

import com.hikmetcakir.coreapi.dto.category.CategorySaveRequest;
import com.hikmetcakir.coreapi.entity.CategoryEntity;
import com.hikmetcakir.coreapi.respository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<String> getAllChildCategoryIds(String parentId) {
        List<String> ids = new ArrayList<>();
        List<CategoryEntity> children = categoryRepository.findByParentId(parentId);
        for (CategoryEntity child : children) {
            ids.add(child.getId());
            ids.addAll(getAllChildCategoryIds(child.getId()));
        }
        return ids;
    }

    public String save(CategorySaveRequest request) {
        CategoryEntity categoryEntity = CategoryEntity.builder()
                .name(request.getName())
                .parentId(request.getParentId())
                .deleted(false)
                .build();
        return Optional.of(categoryEntity)
                .map(categoryRepository::save)
                .map(CategoryEntity::getId)
                .orElseThrow();
    }
 
}
