package com.hikmetcakir.coreapi.service;

import com.hikmetcakir.coreapi.entity.CategoryEntity;
import com.hikmetcakir.coreapi.respository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
