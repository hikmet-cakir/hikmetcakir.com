package com.hikmetcakir.analyticsapi.consumer;

import com.hikmetcakir.analyticsapi.dto.event.ArticleViewEvent;
import com.hikmetcakir.analyticsapi.dto.event.CategoryViewEvent;
import com.hikmetcakir.analyticsapi.service.ArticleViewService;
import com.hikmetcakir.analyticsapi.service.CategoryViewService;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CategoryViewConsumer {

    private final CategoryViewService categoryViewService;

    @KafkaListener(topics = "category-view", groupId = "analytics-group", containerFactory = "categoryViewConcurrentKafkaListenerContainerFactory")
    public void consume(CategoryViewEvent event) {
        categoryViewService.saveViewEvent(event);
    }
}
