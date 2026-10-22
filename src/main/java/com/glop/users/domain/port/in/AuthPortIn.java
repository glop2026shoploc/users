package com.glop.users.domain.port.in;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.CredentialsModel;
import com.glop.users.config.security.CustomUserDetails;
import com.glop.users.domain.model.UserModel;
import org.apache.coyote.BadRequestException;

public interface AuthPortIn {
    AuthenticationModel register(UserModel userModel) throws BadRequestException;
    AuthenticationModel login(CredentialsModel credentialsModel) throws Exception;

    /**This method should be only used in the jwtFilterContext
     *
     * @param email
     * @return
     */
    UserModel findUserModelByUserEmail(String email);
}
