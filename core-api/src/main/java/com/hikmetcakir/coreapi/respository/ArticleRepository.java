package com.hikmetcakir.coreapi.respository;

import com.hikmetcakir.coreapi.entity.ArticleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ArticleRepository extends MongoRepository<ArticleEntity, String> {
}
