package com.glop.users.in.restapi.mapper;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.CredentialsModel;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.model.UserRole;
import com.glop.users.in.restapi.payload.auth.CredentialsPayload;
import com.glop.users.in.restapi.payload.auth.TokenResponse;
import com.glop.users.in.restapi.payload.auth.UserCreationPayload;
import com.glop.users.in.restapi.payload.user.UserRoleEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AuthMapper {

    @Mapping(target = "type", source = "type")
    @Mapping(target = "token", source = "token")
    TokenResponse toTokenResponse(AuthenticationModel authenticationModel);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "encodedPassword", ignore = true)
    @Mapping(target = "dataUsageConsent", source = "dataUsageConsent")
    UserModel toUserModel(UserCreationPayload userCreationPayload);

    @Mapping(target = "role", source = "role")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "encodedPassword", ignore = true)
    CredentialsModel toCredentialsModel(CredentialsPayload credentialsPayload);

    default UserRole toUserRole(UserRoleEnum userRoleEnum){
        return switch (userRoleEnum){
            case ADMIN -> UserRole.ADMIN;
            case MERCHANT ->UserRole.MERCHANT;
            case CLIENT -> UserRole.CLIENT;
        };
    }
}
