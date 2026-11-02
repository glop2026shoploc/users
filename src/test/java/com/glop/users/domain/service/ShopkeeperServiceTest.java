package com.glop.users.domain.service;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.port.out.DocumentPortOut;
import com.glop.users.domain.port.out.ShopkeeperPortOut;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShopkeeperServiceTest {

    @Mock
    private ShopkeeperPortOut shopkeeperPortOut;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DocumentPortOut documentPortOut;

    private ShopkeeperService shopkeeperService;

    private ShopkeeperModel shopkeeper;

    @BeforeEach
    void setUp() {
        this.shopkeeperService = new ShopkeeperService(passwordEncoder, shopkeeperPortOut, documentPortOut);

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

    @Test
    public void testGetWaitingShopkeepersShouldAskForWaitingStatus() {
        when(shopkeeperPortOut.findByStatus(ShopkeeperStatus.WAITING)).thenReturn(List.of(shopkeeper));

        List<ShopkeeperModel> result = shopkeeperService.getWaitingShopkeepers();

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getEmail()).isEqualTo("moncommercant@example.fr");
    }

    @Test
    public void testUpdateStatusShouldThrowErrorWhenNewStatusIsWaiting() {
        assertThatThrownBy(() -> shopkeeperService.updateStatus(1L, ShopkeeperStatus.WAITING))
                .isInstanceOf(BadRequestException.class);

        verify(shopkeeperPortOut, never()).updateStatus(any(), any());
    }

    @Test
    public void testUpdateStatusShouldThrowErrorWhenShopkeeperNotFound() {
        when(shopkeeperPortOut.findById(1L)).thenReturn(null);

        assertThatThrownBy(() -> shopkeeperService.updateStatus(1L, ShopkeeperStatus.VALIDATED))
                .isInstanceOf(ResponseStatusException.class);

        verify(shopkeeperPortOut, never()).updateStatus(any(), any());
    }

    @Test
    public void testUpdateStatusShouldThrowErrorWhenAlreadyValidatedOrRejected() {
        shopkeeper.setStatus(ShopkeeperStatus.REJECTED);
        when(shopkeeperPortOut.findById(1L)).thenReturn(shopkeeper);

        assertThatThrownBy(() -> shopkeeperService.updateStatus(1L, ShopkeeperStatus.VALIDATED))
                .isInstanceOf(BadRequestException.class);

        verify(shopkeeperPortOut, never()).updateStatus(any(), any());
    }

    @Test
    public void testUpdateStatusShouldSaveNewStatus() throws BadRequestException {
        shopkeeper.setStatus(ShopkeeperStatus.WAITING);
        when(shopkeeperPortOut.findById(1L)).thenReturn(shopkeeper);

        shopkeeperService.updateStatus(1L, ShopkeeperStatus.VALIDATED);

        verify(shopkeeperPortOut).updateStatus(1L, ShopkeeperStatus.VALIDATED);
    }

    @Test
    public void testUploadDocumentShouldThrowErrorWhenShopkeeperNotFound() {
        when(shopkeeperPortOut.findById(1L)).thenReturn(null);

        assertThatThrownBy(() -> shopkeeperService.uploadDocument(1L, new byte[]{1}, "application/pdf"))
                .isInstanceOf(ResponseStatusException.class);

        verifyNoInteractions(documentPortOut);
    }

    @Test
    public void testUploadDocumentShouldThrowErrorWhenAlreadyValidatedOrRejected() {
        shopkeeper.setStatus(ShopkeeperStatus.VALIDATED);
        when(shopkeeperPortOut.findById(1L)).thenReturn(shopkeeper);

        assertThatThrownBy(() -> shopkeeperService.uploadDocument(1L, new byte[]{1}, "application/pdf"))
                .isInstanceOf(BadRequestException.class);

        verifyNoInteractions(documentPortOut);
    }

    @Test
    public void testUploadDocumentShouldThrowErrorWhenTypeIsNotAllowed() {
        shopkeeper.setStatus(ShopkeeperStatus.WAITING);
        when(shopkeeperPortOut.findById(1L)).thenReturn(shopkeeper);

        assertThatThrownBy(() -> shopkeeperService.uploadDocument(1L, new byte[]{1}, "text/plain"))
                .isInstanceOf(BadRequestException.class);

        verifyNoInteractions(documentPortOut);
    }

    @Test
    public void testUploadDocumentShouldStoreFileInShopkeeperFolder() throws BadRequestException {
        shopkeeper.setStatus(ShopkeeperStatus.WAITING);
        when(shopkeeperPortOut.findById(1L)).thenReturn(shopkeeper);
        byte[] content = new byte[]{1, 2, 3};

        shopkeeperService.uploadDocument(1L, content, "application/pdf");

        verify(documentPortOut).upload(startsWith("shopkeepers/1/"), eq(content), eq("application/pdf"));
    }
}
