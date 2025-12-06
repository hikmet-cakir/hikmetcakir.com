package com.hikmetcakir.coreapi.repository;

import com.hikmetcakir.coreapi.entity.ArticleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository extends MongoRepository<ArticleEntity, String> {
}
