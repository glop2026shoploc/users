package com.glop.users.config.security;

import com.glop.users.domain.model.UserModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JwtAuthMapperTest {

    private final JwtAuthMapper mapper = new JwtAuthMapper();

    @Test
    public void testToCustomUserDetailsAddsRolePrefix() {
        UserModel user = new UserModel();
        user.setEmail("marius@example.fr");
        user.setRoles(List.of("ADMIN"));

        CustomUserDetails details = mapper.toCustomUserDetails(user);

        assertThat(details.getEmail()).isEqualTo("marius@example.fr");
        assertThat(details.getAuthorities()).extracting("authority").containsExactly("ROLE_ADMIN");
    }

    @Test
    public void testToCustomUserDetailsWithoutRolesGivesNoAuthority() {
        UserModel user = new UserModel();
        user.setEmail("john.doe@example.fr");

        CustomUserDetails details = mapper.toCustomUserDetails(user);

        assertThat(details.getAuthorities()).isEmpty();
    }
}
