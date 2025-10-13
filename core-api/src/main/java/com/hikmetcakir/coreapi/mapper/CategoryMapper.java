package com.hikmetcakir.coreapi.mapper;

import com.hikmetcakir.coreapi.dto.category.Category;
import com.hikmetcakir.coreapi.dto.category.CategoryUpdateRequest;
import com.hikmetcakir.coreapi.entity.CategoryEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category to(CategoryEntity categoryEntity);

    List<Category> to(List<CategoryEntity> categoryEntityList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CategoryUpdateRequest request, @MappingTarget CategoryEntity entity);
}
