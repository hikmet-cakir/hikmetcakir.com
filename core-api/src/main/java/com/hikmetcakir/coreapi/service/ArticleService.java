package com.hikmetcakir.coreapi.service;

import com.hikmetcakir.coreapi.dto.article.ArticleQueryRequest;
import com.hikmetcakir.coreapi.dto.article.ArticleSaveRequest;
import com.hikmetcakir.coreapi.dto.article.ArticleSummary;
import com.hikmetcakir.coreapi.dto.article.ArticleUpdateRequest;
import com.hikmetcakir.coreapi.entity.ArticleEntity;
import com.hikmetcakir.coreapi.mapper.ArticleMapper;
import com.hikmetcakir.coreapi.respository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleService {

    private ArticleRepository articleRepository;

    public List<ArticleSummary> query(ArticleQueryRequest request) {
        ArticleEntity filter  = ArticleEntity.builder()
                .id(request.getId())
                .title(request.getTitle())
                .topicId(request.getTopicId())
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withMatcher(ArticleEntity.Fields.title, match -> match.stringMatcher(ExampleMatcher.StringMatcher.CONTAINING))
                .withMatcher(ArticleEntity.Fields.id, ExampleMatcher.GenericPropertyMatcher::exact)
                .withMatcher(ArticleEntity.Fields.topicId, ExampleMatcher.GenericPropertyMatcher::exact);

        Example<ArticleEntity> example = Example.of(filter, matcher);

        List<ArticleEntity> articleEntityList = articleRepository.findAll(example);
        return ArticleMapper.INSTANCE.to(articleEntityList);
    }

    public String save(ArticleSaveRequest request) {
        ArticleEntity articleEntity = ArticleEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .topicId(request.getTopicId())
                .createdBy(request.getCreatedBy())
                .created(LocalDateTime.now())
                .deleted(false)
                .build();
        return Optional.of(articleEntity)
                .map(articleRepository::save)
                .map(ArticleEntity::getId)
                .orElseThrow();
    }

    public void update(Long id, ArticleUpdateRequest request) {

    }

    public void delete(Long id) {

    }
}
