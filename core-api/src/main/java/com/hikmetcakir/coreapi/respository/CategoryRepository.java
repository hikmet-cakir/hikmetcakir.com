package com.hikmetcakir.coreapi.respository;

import com.hikmetcakir.coreapi.entity.CategoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CategoryRepository extends MongoRepository<CategoryEntity, String> {

    List<CategoryEntity> findByParentId(String parentId);

    List<CategoryEntity> findByParentIdIsNull();

    List<CategoryEntity> findByDeleted(boolean deleted);

    List<CategoryEntity> findByIdInAndDeleted(List<String> idList, boolean deleted);
}
