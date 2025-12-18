package com.hikmetcakir.analyticsapi.repository;

import com.hikmetcakir.analyticsapi.entity.CategoryViewEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryViewRepository extends MongoRepository<CategoryViewEntity, String> {
}
