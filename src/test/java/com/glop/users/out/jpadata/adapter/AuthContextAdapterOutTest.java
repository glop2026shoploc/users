package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.AuthContext;
import com.glop.users.out.jpadata.entity.ClientEntity;
import com.glop.users.out.jpadata.entity.EmployeeEntity;
import com.glop.users.out.jpadata.mapper.AuthContextOutMapper;
import com.glop.users.out.jpadata.repository.ClientRepository;
import com.glop.users.out.jpadata.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthContextAdapterOutTest {
    private static final UUID USER_ID = UUID.randomUUID();

    @Mock
    private AuthContextOutMapper authContextOutMapper;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private AuthContextAdapterOut adapter;

    @Test
    void testGetMerchantContextShouldMapEmployee() {
        EmployeeEntity employee = new EmployeeEntity();
        AuthContext context = new AuthContext();
        when(employeeRepository.findOptionalByUser_UserId(USER_ID)).thenReturn(Optional.of(employee));
        when(authContextOutMapper.toAuthContext(employee)).thenReturn(context);

        assertThat(adapter.getMerchantContext(USER_ID)).contains(context);
    }

    @Test
    void testGetClientContextShouldMapClient() {
        ClientEntity client = new ClientEntity();
        AuthContext context = new AuthContext();
        when(clientRepository.findOptionalByUser_UserId(USER_ID)).thenReturn(Optional.of(client));
        when(authContextOutMapper.toAuthContext(client)).thenReturn(context);

        assertThat(adapter.getClientContext(USER_ID)).contains(context);
    }

    @Test
    void testGetAdminContextIsNotImplementedYet() {
        assertThat(adapter.getAdminContext(USER_ID)).isEmpty();
    }
}
