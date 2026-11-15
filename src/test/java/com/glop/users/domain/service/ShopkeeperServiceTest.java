package com.glop.users.domain.service;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.model.UserRole;
import com.glop.users.domain.model.address.AddressModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.domain.port.out.ReferentialPortOut;
import com.glop.users.domain.port.out.ShopPortOut;
import com.glop.users.domain.port.out.ShopkeeperPortOut;
import com.glop.users.domain.port.out.UserPortOut;
import com.glop.users.exception.UnknownReferenceException;
import com.glop.users.exception.UserNotFoundException;
import com.glop.users.exception.credential.ShopAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShopkeeperServiceTest {

    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID CATEGORY_ID = UUID.randomUUID();
    private static final UUID CITY_ID = UUID.randomUUID();
    private static final UUID SHOP_ID = UUID.randomUUID();

    @Mock
    private AccountService accountService;
    @Mock
    private UserPortOut userPortOut;
    @Mock
    private ShopkeeperPortOut shopkeeperPortOut;
    @Mock
    private ReferentialPortOut referentialPortOut;
    @Mock
    private ShopPortOut shopPortOut;
    @Mock
    private AuthService authService;

    private ShopkeeperService shopkeeperService;

    private ShopkeeperModel shop;
    private UserModel owner;

    @BeforeEach
    void setUp() {
        shopkeeperService = new ShopkeeperService(accountService, userPortOut, shopkeeperPortOut,
                shopPortOut, referentialPortOut, authService);

        owner = new UserModel();
        owner.setUserId(USER_ID);
        owner.setEmail("moncommercant@example.fr");

        UserModel account = new UserModel();
        account.setEmail("moncommercant@example.fr");
        account.setPassword("AStrongPassword123!");

        shop = new ShopkeeperModel();
        shop.setOwner(account);
        shop.setCompanyName("MonCommercant");
        shop.setCategoryId(CATEGORY_ID);
        shop.setSiret("12345678900013");
        shop.setAddress(AddressModel.builder()
                .road("rue de la Republique")
                .number("12")
                .cityModel(CityModel.builder().id(CITY_ID).build())
                .build());
    }

    /** Simule catalogue : renvoie le magasin avec un id. */
    private void catalogueCreatesShop() {
        when(shopPortOut.create(any())).thenAnswer(invocation -> {
            ShopkeeperModel sent = invocation.getArgument(0);
            ShopkeeperModel created = new ShopkeeperModel();
            created.setShopId(SHOP_ID);
            created.setCompanyName(sent.getCompanyName());
            created.setSiret(sent.getSiret());
            created.setCategoryId(sent.getCategoryId());
            created.setAddress(sent.getAddress());
            created.setStatus(ShopkeeperStatus.WAITING);
            return created;
        });
    }

    // ---------- register ----------

    @Test
    void testRegisterShouldCreateAccountShopAndOwnerThenReturnToken() {
        when(referentialPortOut.cityExists(CITY_ID)).thenReturn(true);
        when(accountService.createAccount(shop.getOwner())).thenReturn(owner);
        when(shopkeeperPortOut.existsByOwner(USER_ID)).thenReturn(false);
        catalogueCreatesShop();
        AuthenticationModel token = AuthenticationModel.builder().type("Bearer").token("token").build();
        when(authService.login(USER_ID, UserRole.MERCHANT)).thenReturn(token);

        AuthenticationModel result = shopkeeperService.register(shop);

        assertThat(result).isEqualTo(token);
        assertThat(shop.getOwner()).isEqualTo(owner);
        assertThat(shop.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
        verify(shopPortOut).create(shop);
        verify(shopkeeperPortOut).addOwner(USER_ID, SHOP_ID);
    }

    @Test
    void testRegisterShouldNotAddOwnerWhenCatalogueRejectsSiret() {
        when(referentialPortOut.cityExists(CITY_ID)).thenReturn(true);
        when(accountService.createAccount(shop.getOwner())).thenReturn(owner);
        when(shopkeeperPortOut.existsByOwner(USER_ID)).thenReturn(false);
        when(shopPortOut.create(any())).thenThrow(new ShopAlreadyExistsException("SIRET"));

        assertThatThrownBy(() -> shopkeeperService.register(shop))
                .isInstanceOf(ShopAlreadyExistsException.class);

        verify(shopkeeperPortOut, never()).addOwner(any(), any());
        verifyNoInteractions(authService);
    }

    @Test
    void testRegisterShouldThrowAndCreateNothingWhenCityIsUnknown() {
        when(referentialPortOut.cityExists(CITY_ID)).thenReturn(false);

        assertThatThrownBy(() -> shopkeeperService.register(shop))
                .isInstanceOf(UnknownReferenceException.class);

        verifyNoInteractions(accountService, authService, shopPortOut);
        verify(shopkeeperPortOut, never()).addOwner(any(), any());
    }

    // ---------- create ----------

    @Test
    void testCreateShouldAttachShopToExistingUser() {
        when(userPortOut.findById(USER_ID)).thenReturn(Optional.of(owner));
        when(referentialPortOut.cityExists(CITY_ID)).thenReturn(true);
        when(shopkeeperPortOut.existsByOwner(USER_ID)).thenReturn(false);
        catalogueCreatesShop();

        ShopkeeperModel result = shopkeeperService.create(USER_ID, shop);

        assertThat(result.getShopId()).isEqualTo(SHOP_ID);
        assertThat(result.getOwner()).isEqualTo(owner);
        assertThat(result.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
        verify(shopkeeperPortOut).addOwner(USER_ID, SHOP_ID);
        verifyNoInteractions(accountService, authService);
    }

    @Test
    void testCreateShouldThrowWhenUserNotFound() {
        when(userPortOut.findById(USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> shopkeeperService.create(USER_ID, shop))
                .isInstanceOf(UserNotFoundException.class);

        verifyNoInteractions(shopPortOut);
    }

    @Test
    void testCreateShouldThrowWhenUserAlreadyOwnsAShop() {
        when(userPortOut.findById(USER_ID)).thenReturn(Optional.of(owner));
        when(referentialPortOut.cityExists(CITY_ID)).thenReturn(true);
        when(shopkeeperPortOut.existsByOwner(USER_ID)).thenReturn(true);

        assertThatThrownBy(() -> shopkeeperService.create(USER_ID, shop))
                .isInstanceOf(ShopAlreadyExistsException.class);

        verifyNoInteractions(shopPortOut);
    }
}
