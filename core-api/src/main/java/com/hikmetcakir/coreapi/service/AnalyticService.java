package com.hikmetcakir.coreapi.service;

import com.hikmetcakir.coreapi.dto.event.ArticleViewEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AnalyticService {

    private final KafkaTemplate<String, ArticleViewEvent> viewTemplate;

    public void sendViewEvent(ArticleViewEvent event) {
        viewTemplate.send("article-view", event.getArticleId(), event);
    }
}
