package com.glop.controller;

import com.glop.model.UsersModel;
import com.glop.service.IUsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController {
    IUsersService usersService;
    @GetMapping("/me")
    public ResponseEntity<UsersModel> me(@AuthenticationPrincipal Jwt jwt) {
        UUID keycloakId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(usersService.findCurrentUsers(keycloakId));
    }
}
