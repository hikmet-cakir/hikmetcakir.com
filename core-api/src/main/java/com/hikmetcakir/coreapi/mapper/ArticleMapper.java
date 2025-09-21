package com.hikmetcakir.coreapi.mapper;

import com.hikmetcakir.coreapi.dto.article.ArticleSummary;
import com.hikmetcakir.coreapi.entity.ArticleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {

    ArticleMapper INSTANCE = Mappers.getMapper(ArticleMapper.class);

    ArticleSummary to(ArticleEntity articleEntity);

    List<ArticleSummary> to(List<ArticleEntity> articleEntityList);
}
