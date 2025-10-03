package com.hikmetcakir.coreapi.respository;

import com.hikmetcakir.coreapi.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByEmailAndPassword(String email, String password);
}
