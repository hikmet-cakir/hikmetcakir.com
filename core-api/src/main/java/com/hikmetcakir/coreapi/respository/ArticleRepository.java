package com.hikmetcakir.coreapi.respository;

import com.hikmetcakir.coreapi.entity.ArticleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends MongoRepository<ArticleEntity, String> {
}
