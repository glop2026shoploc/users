package com.glop.users.domain.service;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.model.UserRole;
import com.glop.users.domain.model.address.AddressModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.domain.port.out.ReferentialPortOut;
import com.glop.users.domain.port.out.ShopkeeperPortOut;
import com.glop.users.domain.port.out.TokenPortOut;
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

    @Mock
    private AccountService accountService;
    @Mock
    private UserPortOut userPortOut;
    @Mock
    private ShopkeeperPortOut shopkeeperPortOut;
    @Mock
    private ReferentialPortOut referentialPortOut;
    @Mock
    private TokenPortOut tokenPortOut;
    @Mock
    private AuthService authService;

    private ShopkeeperService shopkeeperService;

    private ShopkeeperModel shop;
    private UserModel owner;

    @BeforeEach
    void setUp() {
        shopkeeperService = new ShopkeeperService(accountService, userPortOut, shopkeeperPortOut,
                referentialPortOut, tokenPortOut, authService);

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

    private void validReferences() {
        when(shopkeeperPortOut.existsBySiret(shop.getSiret())).thenReturn(false);
        when(referentialPortOut.categoryExists(CATEGORY_ID)).thenReturn(true);
        when(referentialPortOut.cityExists(CITY_ID)).thenReturn(true);
    }

    // ---------- register ----------

    @Test
    void testRegisterShouldCreateAccountAndShopThenReturnToken() {
        validReferences();
        when(accountService.createAccount(shop.getOwner())).thenReturn(owner);
        when(shopkeeperPortOut.existsByOwner(USER_ID)).thenReturn(false);
        when(shopkeeperPortOut.create(any())).thenAnswer(invocation -> invocation.getArgument(0));
        AuthenticationModel token = AuthenticationModel.builder().type("Bearer").token("token").build();
        when(authService.login(USER_ID, UserRole.MERCHANT)).thenReturn(token);

        AuthenticationModel result = shopkeeperService.register(shop);

        assertThat(result).isEqualTo(token);
        assertThat(shop.getOwner()).isEqualTo(owner);
        assertThat(shop.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
        verify(shopkeeperPortOut).create(shop);
    }

    @Test
    void testRegisterShouldThrowAndCreateNothingWhenSiretAlreadyExists() {
        when(shopkeeperPortOut.existsBySiret(shop.getSiret())).thenReturn(true);

        assertThatThrownBy(() -> shopkeeperService.register(shop))
                .isInstanceOf(ShopAlreadyExistsException.class);

        verifyNoInteractions(accountService, authService);
        verify(shopkeeperPortOut, never()).create(any());
    }

    @Test
    void testRegisterShouldThrowAndCreateNothingWhenCityIsUnknown() {
        when(shopkeeperPortOut.existsBySiret(shop.getSiret())).thenReturn(false);
        when(referentialPortOut.categoryExists(CATEGORY_ID)).thenReturn(true);
        when(referentialPortOut.cityExists(CITY_ID)).thenReturn(false);

        assertThatThrownBy(() -> shopkeeperService.register(shop))
                .isInstanceOf(UnknownReferenceException.class);

        verifyNoInteractions(accountService, authService);
        verify(shopkeeperPortOut, never()).create(any());
    }

    @Test
    void testRegisterShouldRemoveUnknownCategory() {
        when(shopkeeperPortOut.existsBySiret(shop.getSiret())).thenReturn(false);
        when(referentialPortOut.categoryExists(CATEGORY_ID)).thenReturn(false);
        when(referentialPortOut.cityExists(CITY_ID)).thenReturn(true);
        when(accountService.createAccount(any())).thenReturn(owner);
        when(shopkeeperPortOut.existsByOwner(USER_ID)).thenReturn(false);
        when(shopkeeperPortOut.create(any())).thenAnswer(invocation -> invocation.getArgument(0));

        shopkeeperService.register(shop);

        assertThat(shop.getCategoryId()).isNull();
    }

    // ---------- create ----------

    @Test
    void testCreateShouldAttachShopToExistingUser() {
        when(userPortOut.findById(USER_ID)).thenReturn(Optional.of(owner));
        validReferences();
        when(shopkeeperPortOut.existsByOwner(USER_ID)).thenReturn(false);
        when(shopkeeperPortOut.create(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ShopkeeperModel result = shopkeeperService.create(USER_ID, shop);

        assertThat(result.getOwner()).isEqualTo(owner);
        assertThat(result.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
        verifyNoInteractions(accountService, authService);
    }

    @Test
    void testCreateShouldThrowWhenUserNotFound() {
        when(userPortOut.findById(USER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> shopkeeperService.create(USER_ID, shop))
                .isInstanceOf(UserNotFoundException.class);

        verify(shopkeeperPortOut, never()).create(any());
    }

    @Test
    void testCreateShouldThrowWhenUserAlreadyOwnsAShop() {
        when(userPortOut.findById(USER_ID)).thenReturn(Optional.of(owner));
        validReferences();
        when(shopkeeperPortOut.existsByOwner(USER_ID)).thenReturn(true);

        assertThatThrownBy(() -> shopkeeperService.create(USER_ID, shop))
                .isInstanceOf(ShopAlreadyExistsException.class);

        verify(shopkeeperPortOut, never()).create(any());
    }
}