package com.hikmetcakir.analyticsapi.service;

import com.hikmetcakir.analyticsapi.dto.event.CategoryViewEvent;
import com.hikmetcakir.analyticsapi.entity.CategoryViewEntity;
import com.hikmetcakir.analyticsapi.repository.CategoryViewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryViewService {

    private final CategoryViewRepository categoryViewRepository;

    public void saveViewEvent(CategoryViewEvent event) {
        CategoryViewEntity view = CategoryViewEntity.builder()
                .categoryId(event.getCategoryId())
                .timestamp(event.getTimestamp())
                .build();

        categoryViewRepository.save(view);
    }
}
