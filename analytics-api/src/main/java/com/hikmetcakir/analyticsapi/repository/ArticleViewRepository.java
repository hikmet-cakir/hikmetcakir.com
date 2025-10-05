package com.hikmetcakir.analyticsapi.repository;

import com.hikmetcakir.analyticsapi.entity.ArticleViewEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleViewRepository extends MongoRepository<ArticleViewEntity, String> {
}
