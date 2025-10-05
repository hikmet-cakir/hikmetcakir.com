package com.hikmetcakir.analyticsapi.service;

import com.hikmetcakir.analyticsapi.dto.event.ArticleViewEvent;
import com.hikmetcakir.analyticsapi.entity.ArticleViewEntity;
import com.hikmetcakir.analyticsapi.repository.ArticleViewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ArticleViewService {

    private final ArticleViewRepository articleViewRepository;

    public void saveViewEvent(ArticleViewEvent event) {
        ArticleViewEntity view = ArticleViewEntity.builder()
                .articleId(event.getArticleId())
                .userId(event.getUserId())
                .timestamp(event.getTimestamp())
                .build();

        articleViewRepository.save(view);
    }
}
