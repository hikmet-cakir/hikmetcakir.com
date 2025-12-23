package com.hikmetcakir.coreapi.repository;

import com.hikmetcakir.coreapi.entity.ArticleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArticleRepository extends MongoRepository<ArticleEntity, String> {

    @Query("""
    {
      "$or": [
        { "title":   { "$regex": ?0, "$options": "i" } },
        { "content": { "$regex": ?0, "$options": "i" } }
      ]
    }
    """)
    List<ArticleEntity> lookup(@Param("keyword") String keyword);
}
