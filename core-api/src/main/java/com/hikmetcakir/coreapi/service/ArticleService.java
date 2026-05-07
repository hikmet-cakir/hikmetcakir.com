package com.hikmetcakir.coreapi.service;

import com.hikmetcakir.coreapi.dto.article.ArticleQueryRequest;
import com.hikmetcakir.coreapi.dto.article.ArticleSaveRequest;
import com.hikmetcakir.coreapi.dto.article.ArticleSummary;
import com.hikmetcakir.coreapi.dto.article.ArticleUpdateRequest;
import com.hikmetcakir.coreapi.dto.article.ArticleLookupRequest;
import com.hikmetcakir.coreapi.dto.event.ArticleViewEvent;
import com.hikmetcakir.coreapi.dto.event.CategoryViewEvent;
import com.hikmetcakir.coreapi.entity.ArticleEntity;
import com.hikmetcakir.coreapi.mapper.ArticleMapper;
import com.hikmetcakir.coreapi.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.Sort.by;

@Service
@AllArgsConstructor
@Slf4j
public class ArticleService {

    private ArticleRepository articleRepository;

    private CategoryService categoryService;

    private MongoTemplate mongoTemplate;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public List<ArticleSummary> query(ArticleQueryRequest request) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        criteriaList.add(Criteria.where("deleted").is(false));

        if (request.getId() != null) {
            criteriaList.add(Criteria.where("id").is(request.getId()));
            kafkaTemplate.send("article-view", new ArticleViewEvent(request.getId(), LocalDateTime.now()));
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            criteriaList.add(Criteria.where("title").regex(request.getTitle(), "i"));
        }

        if (request.getCategoryId() != null) {
            List<String> categoryIds = new ArrayList<>();
            categoryIds.add(request.getCategoryId());
            categoryIds.addAll(categoryService.getAllChildCategoryIds(request.getCategoryId()));
            criteriaList.add(Criteria.where("categoryId").in(categoryIds));
            kafkaTemplate.send("category-view", new CategoryViewEvent(request.getCategoryId(), LocalDateTime.now()));
        }

        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), by(Direction.DESC, "created"));
        query.with(pageRequest);

        List<ArticleEntity> articleEntityList = mongoTemplate.find(query, ArticleEntity.class);

        return articleEntityList.stream().map(ArticleMapper.INSTANCE::to).toList();
    }

    public String save(ArticleSaveRequest request) {
        ArticleEntity articleEntity = ArticleEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .categoryId(request.getCategoryId())
                .thumbnail(request.getThumbnail())
                .createdBy(request.getCreatedBy())
                .created(LocalDateTime.now())
                .deleted(false)
                .build();
        String articleId = Optional.of(articleEntity)
                .map(articleRepository::save)
                .map(ArticleEntity::getId)
                .orElseThrow();
        log.info("Article saved successfully. articleId={}, title={}, categoryId={}, createdBy={}",
                articleId,
                request.getTitle(),
                request.getCategoryId(),
                request.getCreatedBy()
        );
        return articleId;
    }

    public void update(String id, ArticleUpdateRequest request) {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article with given id does not exist and cannot be updated"));
        log.info("Article update started. articleId={}, oldTitle={}, oldCategoryId={}, updatedBy={}",
                id,
                articleEntity.getTitle(),
                articleEntity.getCategoryId(),
                request.getUpdatedBy()
        );

        ArticleMapper.INSTANCE.updateEntity(request, articleEntity);

        articleRepository.save(articleEntity);

        log.info("Article updated successfully. articleId={}, newTitle={}, newCategoryId={}, updatedBy={}",
                id,
                articleEntity.getTitle(),
                articleEntity.getCategoryId(),
                request.getUpdatedBy()
        );
    }

    public void delete(String id) {
        ArticleEntity articleEntity = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article with given id does not exist and cannot be deleted"));

        articleEntity.setDeleted(true);

        articleRepository.save(articleEntity);
        log.info("Article deleted successfully. articleId={}", id);
    }

    public List<ArticleSummary> lookup(ArticleLookupRequest request) {
        List<ArticleEntity> articleEntityList = articleRepository.lookup(request.getNormalizedKeyword());
        return articleEntityList.stream().map(ArticleMapper.INSTANCE::to).toList();
    }
}
