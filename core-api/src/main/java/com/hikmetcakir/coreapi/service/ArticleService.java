package com.hikmetcakir.coreapi.service;

import com.hikmetcakir.coreapi.dto.article.ArticleQueryRequest;
import com.hikmetcakir.coreapi.dto.article.ArticleSaveRequest;
import com.hikmetcakir.coreapi.dto.article.ArticleSummary;
import com.hikmetcakir.coreapi.dto.article.ArticleUpdateRequest;
import com.hikmetcakir.coreapi.dto.event.ArticleViewEvent;
import com.hikmetcakir.coreapi.entity.ArticleEntity;
import com.hikmetcakir.coreapi.mapper.ArticleMapper;
import com.hikmetcakir.coreapi.respository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleService {

    private ArticleRepository articleRepository;

    private CategoryService categoryService;

    private MongoTemplate mongoTemplate;

    private final KafkaTemplate<String, ArticleViewEvent> viewKafkaTemplate;

    public List<ArticleSummary> query(ArticleQueryRequest request) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (request.getId() != null) {
            criteriaList.add(Criteria.where("id").is(request.getId()));
            viewKafkaTemplate.send("article-view", new ArticleViewEvent(request.getId(), "49001", LocalDateTime.now()));
        }
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            criteriaList.add(Criteria.where("title").regex(request.getTitle(), "i"));
        }

        if (request.getCategoryId() != null) {
            List<String> categoryIds = new ArrayList<>();
            categoryIds.add(request.getCategoryId());
            categoryIds.addAll(categoryService.getAllChildCategoryIds(request.getCategoryId()));
            criteriaList.add(Criteria.where("categoryId").in(categoryIds));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize());
        query.with(pageRequest);

        List<ArticleEntity> articleEntityList = mongoTemplate.find(query, ArticleEntity.class);

        return articleEntityList.stream().map(ArticleMapper.INSTANCE::to).toList();
    }

    public String save(ArticleSaveRequest request) {
        ArticleEntity articleEntity = ArticleEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .categoryId(request.getCategoryId())
                .createdBy(request.getCreatedBy())
                .created(LocalDateTime.now())
                .deleted(false)
                .build();
        return Optional.of(articleEntity)
                .map(articleRepository::save)
                .map(ArticleEntity::getId)
                .orElseThrow();
    }

    public void update(String id, ArticleUpdateRequest request) {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article with given id does not exist and cannot be updated"));

        ArticleMapper.INSTANCE.updateEntity(request, articleEntity);

        articleRepository.save(articleEntity);
    }

    public void delete(String id) {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article with given id does not exist and cannot be deleted"));

        articleEntity.setDeleted(true);

        articleRepository.save(articleEntity);
    }
}
