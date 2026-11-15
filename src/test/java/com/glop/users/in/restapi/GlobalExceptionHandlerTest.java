package com.glop.users.in.restapi;

import com.glop.users.exception.CatalogueException;
import com.glop.users.exception.UnknownReferenceException;
import com.glop.users.exception.UserNotFoundException;
import com.glop.users.exception.credential.AlreadyRegisterException;
import com.glop.users.exception.credential.InvalidCredentialsException;
import com.glop.users.exception.credential.ShopAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testConflictExceptionsShouldReturn409() {
        assertThat(handler.handleConflict(new ShopAlreadyExistsException("SIRET")).getStatus())
                .isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(handler.handleConflict(new AlreadyRegisterException()).getStatus())
                .isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    void testCatalogueExceptionShouldReturn502() {
        ProblemDetail problem = handler.handleCatalogue(new CatalogueException("Service catalogue injoignable."));

        assertThat(problem.getStatus()).isEqualTo(HttpStatus.BAD_GATEWAY.value());
        assertThat(problem.getDetail()).isEqualTo("Service catalogue injoignable.");
    }

    @Test
    void testOtherBusinessExceptionsShouldReturnTheirStatus() {
        assertThat(handler.handleUnknownReference(new UnknownReferenceException("Ville inconnue.")).getStatus())
                .isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(handler.handleNotFound(new UserNotFoundException()).getStatus())
                .isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(handler.handleInvalidCredentials(new InvalidCredentialsException()).getStatus())
                .isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void testValidationShouldListInvalidFields() throws NoSuchMethodException {
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(new Object(), "payload");
        result.addError(new FieldError("payload", "siret", "doit contenir 14 chiffres"));
        MethodParameter parameter = new MethodParameter(Object.class.getMethod("equals", Object.class), 0);

        ProblemDetail problem = handler.handleValidation(new MethodArgumentNotValidException(parameter, result));

        assertThat(problem.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(problem.getProperties().get("errors")).isEqualTo(Map.of("siret", "doit contenir 14 chiffres"));
    }
}
