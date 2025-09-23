package com.hikmetcakir.coreapi.mapper;

import com.hikmetcakir.coreapi.dto.article.ArticleSummary;
import com.hikmetcakir.coreapi.dto.article.ArticleUpdateRequest;
import com.hikmetcakir.coreapi.entity.ArticleEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    ArticleSummary to(ArticleEntity articleEntity);

    List<ArticleSummary> to(List<ArticleEntity> articleEntityList);

    @Mapping(target = "updated", expression = "java(java.time.LocalDateTime.now())")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(ArticleUpdateRequest request, @MappingTarget ArticleEntity entity);
}
