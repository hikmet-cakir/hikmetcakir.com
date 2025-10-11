package com.hikmetcakir.coreapi.service;

import com.hikmetcakir.coreapi.dto.category.Category;
import com.hikmetcakir.coreapi.dto.category.CategoryHierarchy;
import com.hikmetcakir.coreapi.dto.category.CategorySaveRequest;
import com.hikmetcakir.coreapi.entity.CategoryEntity;
import com.hikmetcakir.coreapi.mapper.CategoryMapper;
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

    public List<CategoryHierarchy> queryCategoryHierarchy(List<Integer> levels) {
        if (levels == null || levels.isEmpty()) {
            return List.of();
        }

        int maxLevel = levels.stream().max(Integer::compareTo).orElse(1);

        List<CategoryEntity> roots = categoryRepository.findByParentIdIsNull();

        return roots.stream()
                .map(root -> mapToDto(root, 1, maxLevel))
                .toList();
    }

    private CategoryHierarchy mapToDto(CategoryEntity entity, int currentLevel, int maxLevel) {
        List<CategoryHierarchy> children = List.of();

        if (currentLevel < maxLevel) {
            List<CategoryEntity> childEntities = categoryRepository.findByParentId(entity.getId());
            children = childEntities.stream()
                    .map(child -> mapToDto(child, currentLevel + 1, maxLevel))
                    .toList();
        }

        return CategoryHierarchy.builder()
                .id(entity.getId())
                .name(entity.getName())
                .children(children)
                .build();
    }

    public List<Category> query() {
        List<CategoryEntity> all = categoryRepository.findAll();
        return CategoryMapper.INSTANCE.to(all);
    }

    public void delete(String id) {
        List<String> categoryIds = new ArrayList<>();
        categoryIds.add(id);
        categoryIds.addAll(getAllChildCategoryIds(id));

        List<CategoryEntity> categoryEntityList = categoryRepository.findByIdInAndDeleted(categoryIds, false);
        categoryEntityList.forEach(categoryEntity -> categoryEntity.setDeleted(true));

        categoryRepository.saveAll(categoryEntityList);
    }
}
