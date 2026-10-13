package com.glop.users.in.restapi.mapper;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.CredentialsModel;
import com.glop.users.domain.model.UserModel;
import com.glop.users.in.restapi.payload.CredentialsPayload;
import com.glop.users.in.restapi.payload.TokenResponse;
import com.glop.users.in.restapi.payload.UserCreationPayload;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(source = "token", target = "token")
    @Mapping(source = "type", target = "type")
    TokenResponse toTokenResponse(AuthenticationModel authenticationModel);

    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(ignore = true, target = "encodedPassword")
    UserModel toUserModel(UserCreationPayload userCreationPayload);

    @Mapping(target = "encodedPassword", ignore = true)
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    CredentialsModel toUserModel(CredentialsPayload credentialsPayload);
}
