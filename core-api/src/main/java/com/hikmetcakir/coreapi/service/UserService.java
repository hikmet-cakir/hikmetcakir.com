package com.hikmetcakir.coreapi.service;

import com.hikmetcakir.coreapi.dto.user.UserQueryRequest;
import com.hikmetcakir.coreapi.dto.user.UserQueryResponse;
import com.hikmetcakir.coreapi.mapper.UserMapper;
import com.hikmetcakir.coreapi.respository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserQueryResponse query(UserQueryRequest request) {
        return userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .map(UserMapper.INSTANCE::to)
                .orElse(null);
    }
}
