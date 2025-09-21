package com.hikmetcakir.coreapi.service;

import com.hikmetcakir.coreapi.dto.article.ArticleQueryRequest;
import com.hikmetcakir.coreapi.dto.article.ArticleSaveRequest;
import com.hikmetcakir.coreapi.dto.article.ArticleSummary;
import com.hikmetcakir.coreapi.dto.article.ArticleUpdateRequest;
import com.hikmetcakir.coreapi.entity.ArticleEntity;
import com.hikmetcakir.coreapi.respository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleService {

    private ArticleRepository articleRepository;

    public List<ArticleSummary> query(ArticleQueryRequest request) {
        return null;
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
