package com.glop.users.out.jpadata.mapper;

import com.glop.users.domain.model.AuthContext;
import com.glop.users.domain.model.EmployeeRole;
import com.glop.users.domain.model.UserRole;
import com.glop.users.out.jpadata.entity.*;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AuthContextOutMapperTest {
    private final AuthContextOutMapper mapper = new AuthContextOutMapperImpl();

    private static UserEntity user() {
        UserEntity user = new UserEntity();
        user.setUserId(UUID.randomUUID());
        user.setEmail("test@glop.fr");
        return user;
    }

    private static CityEntity city() {
        CityEntity city = new CityEntity();
        city.setCityId(UUID.randomUUID());
        return city;
    }

    @Test
    void testEmployeeShouldMapShopIdWithoutCity() {
        EmployeeEntity employee = new EmployeeEntity();
        employee.setEmployeeId(UUID.randomUUID());
        employee.setUser(user());
        employee.setShopId(UUID.randomUUID());
        employee.setRole(com.glop.users.out.jpadata.entity.EmployeeRole.OWNER);

        AuthContext context = mapper.toAuthContext(employee);

        assertThat(context.getUserRole()).isEqualTo(UserRole.MERCHANT);
        assertThat(context.getShopId()).isEqualTo(employee.getShopId());
        assertThat(context.getEmployeeId()).isEqualTo(employee.getEmployeeId());
        assertThat(context.getEmployeeRole()).isEqualTo(EmployeeRole.OWNER);
        assertThat(context.getEmail()).isEqualTo("test@glop.fr");
        assertThat(context.getCityId()).isNull();
    }

    @Test
    void testClientShouldMapCity() {
        ClientEntity client = new ClientEntity();
        client.setClientId(UUID.randomUUID());
        client.setUser(user());
        client.setCity(city());

        AuthContext context = mapper.toAuthContext(client);

        assertThat(context.getUserRole()).isEqualTo(UserRole.CLIENT);
        assertThat(context.getClientId()).isEqualTo(client.getClientId());
        assertThat(context.getCityId()).isEqualTo(client.getCity().getCityId());
    }

    @Test
    void testAdminShouldMapCity() {
        AdminEntity admin = new AdminEntity();
        admin.setAdminId(UUID.randomUUID());
        admin.setUser(user());
        admin.setCity(city());

        AuthContext context = mapper.toAuthContext(admin);

        assertThat(context.getUserRole()).isEqualTo(UserRole.ADMIN);
        assertThat(context.getAdminId()).isEqualTo(admin.getAdminId());
        assertThat(context.getCityId()).isEqualTo(admin.getCity().getCityId());
    }

    @Test
    void testToEmployeeRoleShouldConvertEveryValue() {
        assertThat(mapper.toEmployeeRole(com.glop.users.out.jpadata.entity.EmployeeRole.OWNER)).isEqualTo(EmployeeRole.OWNER);
        assertThat(mapper.toEmployeeRole(com.glop.users.out.jpadata.entity.EmployeeRole.EMPLOYEE)).isEqualTo(EmployeeRole.EMPLOYEE);
        assertThat(mapper.toEmployeeRole(null)).isNull();
    }
}
