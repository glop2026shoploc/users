package com.glop.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    @PostMapping("/register")
    public Object register(@RequestBody UserEntity userEntity) throws BadRequestException {
        if(userRepository.findByEmail(userEntity.getEmail())!=null){
            throw new BadRequestException("");
        }
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    @PostMapping("/login")
    public Map<String,Object> login(@RequestBody UserEntity userEntity) throws InvalidCredentialsException {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEntity.getEmail(),userEntity.getPassword()));
            if(authentication.isAuthenticated()){
                Map<String,Object> authData = new HashMap<>();
                authData.put("token",jwtUtils.generateToken(userEntity.getEmail()));
                authData.put("type","Bearer");
                return authData;
            }
            throw new InvalidCredentialsException();
        } catch (AuthenticationException e) {
            log.error(e.getMessage());
            throw new InvalidCredentialsException();
        }
    }
}
