package com.glop.users.out.jpadata.mapper;

import com.glop.users.domain.model.UserModel;
import com.glop.users.out.jpadata.entity.UserCredentialsEntity;
import com.glop.users.out.jpadata.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserOutMapper {

    /** Utilisateur sans mot de passe. */
    @Mapping(target = "userId", source = "userId")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "encodedPassword", ignore = true)
    UserModel toUserModel(UserEntity userEntity);

    /** Utilisateur avec son hash, pour le login. */
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "encodedPassword", source = "password")
    UserModel toUserModel(UserCredentialsEntity credentialsEntity);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "createdAt", ignore = true)
    UserEntity toUserEntity(UserModel userModel);

    /** Nouveau compte : credentials + user (enregistré en cascade). */
    @Mapping(target = "credentialId", ignore = true)
    @Mapping(target = "user", source = "userModel")
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "updatedAt", ignore = true)
    UserCredentialsEntity toCredentialsEntity(UserModel userModel);
}
