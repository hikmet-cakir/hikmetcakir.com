package com.hikmetcakir.analyticsapi.consumer;

import com.hikmetcakir.analyticsapi.dto.event.ArticleViewEvent;
import com.hikmetcakir.analyticsapi.service.ArticleViewService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ArticleViewConsumer {

    private final ArticleViewService articleViewService;

    @KafkaListener(topics = "article-view", groupId = "analytics-group", containerFactory = "articleViewConcurrentKafkaListenerContainerFactory")
    public void consume(ArticleViewEvent event) {
        articleViewService.saveViewEvent(event);
    }
}
