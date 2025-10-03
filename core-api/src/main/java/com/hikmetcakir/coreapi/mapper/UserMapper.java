package com.hikmetcakir.coreapi.mapper;

import com.hikmetcakir.coreapi.dto.user.UserQueryResponse;
import com.hikmetcakir.coreapi.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserQueryResponse to(UserEntity userEntity);
}
