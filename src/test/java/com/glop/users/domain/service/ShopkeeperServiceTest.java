package com.glop.users.domain.service;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.port.out.ShopkeeperPortOut;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShopkeeperServiceTest {

    @Mock
    private ShopkeeperPortOut shopkeeperPortOut;

    @Mock
    private PasswordEncoder passwordEncoder;

    private ShopkeeperService shopkeeperService;

    private ShopkeeperModel shopkeeper;

    @BeforeEach
    void setUp() {
        this.shopkeeperService = new ShopkeeperService(passwordEncoder, shopkeeperPortOut);

        this.shopkeeper = new ShopkeeperModel();
        shopkeeper.setCompanyName("MonCommercant");
        shopkeeper.setCategory("ALIMENTATION");
        shopkeeper.setSiret("12345678900013");
        shopkeeper.setAddress("12 rue de la Republique, 59000 Lille");
        shopkeeper.setEmail("moncommercant@example.fr");
        shopkeeper.setPassword("AStrongPassword123!");
    }

    @Test
    public void testRegisterShouldThrowErrorWhenEmailAlreadyExists() {
        when(shopkeeperPortOut.findByEmail(shopkeeper.getEmail())).thenReturn(new ShopkeeperModel());

        assertThatThrownBy(() -> shopkeeperService.register(shopkeeper))
                .isInstanceOf(BadRequestException.class);

        verify(shopkeeperPortOut, never()).create(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void testRegisterShouldThrowErrorWhenSiretAlreadyExists() {
        when(shopkeeperPortOut.findBySiret(shopkeeper.getSiret())).thenReturn(new ShopkeeperModel());

        assertThatThrownBy(() -> shopkeeperService.register(shopkeeper))
                .isInstanceOf(BadRequestException.class);

        verify(shopkeeperPortOut, never()).create(any());
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    public void testRegisterShouldEncodePasswordAndHaveWaitingStatus() throws BadRequestException {
        when(passwordEncoder.encode("AStrongPassword123!")).thenReturn("hashed-password");
        when(shopkeeperPortOut.create(any())).thenReturn(new ShopkeeperModel());

        shopkeeperService.register(shopkeeper);

        assertThat(shopkeeper.getEncodedPassword()).isEqualTo("hashed-password");
        assertThat(shopkeeper.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
    }

    @Test
    public void testRegisterShouldNotReturnPasswords() throws BadRequestException {
        when(shopkeeperPortOut.create(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ShopkeeperModel result = shopkeeperService.register(shopkeeper);

        assertThat(result.getPassword()).isNull();
        assertThat(result.getEncodedPassword()).isNull();
    }
}
