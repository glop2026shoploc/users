package com.glop.users.out.jpadata.mapper;

import com.glop.users.domain.model.UserModel;
import com.glop.users.out.jpadata.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public abstract class UserOutMapper {

    @Mapping(target = "roles", source = "role")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "encodedPassword", source = "password")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", ignore = true)
    public abstract UserModel toUserModel(UserEntity userEntity);


    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "email", source = "email")
    public abstract UserEntity toUserEntity(UserModel userModel);

    protected List<String> toRoles(String role) {
        List<String> roles = new ArrayList<>();
        if (role != null) {
            roles.add(role);
        }
        return roles;
    }
}
