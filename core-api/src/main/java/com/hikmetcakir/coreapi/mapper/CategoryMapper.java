package com.hikmetcakir.coreapi.mapper;

import com.hikmetcakir.coreapi.dto.category.Category;
import com.hikmetcakir.coreapi.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    Category to(CategoryEntity categoryEntity);

    List<Category> to(List<CategoryEntity> categoryEntityList);
}
