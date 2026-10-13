package com.glop.users.domain.service;

import com.glop.users.config.security.JwtUtils;
import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.CredentialsModel;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.port.in.AuthPortIn;
import com.glop.users.domain.port.out.UserPortOut;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService implements AuthPortIn {
    private final UserPortOut userPortOut;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    @Override
    public AuthenticationModel register(UserModel userModel) throws BadRequestException {
        System.out.println("Register - before check");
        if(userPortOut.findByEmail(userModel.getEmail())!=null){
            throw new BadRequestException("");
        }
        System.out.println("Register - after check");
        userModel.setEncodedPassword(passwordEncoder.encode(userModel.getPassword()));
        userModel = userPortOut.create(userModel);
        return getCredentials(userModel);
    }

    private AuthenticationModel getCredentials(UserModel userModel) {
        return AuthenticationModel.builder()
                .token(jwtUtils.generateToken(userModel))
                .type("Bearer").build();
    }

    @Override
    public AuthenticationModel login(CredentialsModel credentialsModel) throws Exception {
        UserModel userModel = userPortOut.findByEmail(credentialsModel.getEmail());
        if(userModel!=null && passwordEncoder.matches(credentialsModel.getPassword(),userModel.getEncodedPassword())){
            return getCredentials(userModel);
        }
        throw new Exception();
    }

    @Override
    public UserModel findUserModelByUserEmail(String email) {
        UserModel userModel = userPortOut.findByEmail(email);
        if(userModel!=null){
            userModel.setPassword(null);
            userModel.setEncodedPassword(null);
        }
        return userModel;
    }

}
