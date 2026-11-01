package com.glop.users.out.jpadata.mapper;

import com.glop.users.domain.model.UserModel;
import com.glop.users.out.jpadata.entity.UserEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserOutMapperTest {

    private final UserOutMapper mapper = new UserOutMapperImpl();

    @Test
    public void testToUserModelPutsRoleInRoles() {
        UserEntity entity = new UserEntity();
        entity.setEmail("marius@example.fr");
        entity.setRole("ADMIN");

        UserModel model = mapper.toUserModel(entity);

        assertThat(model.getRoles()).containsExactly("ADMIN");
    }

    @Test
    public void testToUserModelWithoutRoleGivesEmptyRoles() {
        UserEntity entity = new UserEntity();
        entity.setEmail("john.doe@example.fr");

        UserModel model = mapper.toUserModel(entity);

        assertThat(model.getRoles()).isEmpty();
    }
}
