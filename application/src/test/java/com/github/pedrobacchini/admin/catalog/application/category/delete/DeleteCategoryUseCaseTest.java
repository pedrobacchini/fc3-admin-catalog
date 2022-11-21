package com.github.pedrobacchini.admin.catalog.application.category.delete;

import com.github.pedrobacchini.admin.catalog.domain.category.CategoryGateway;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase defaultDeleteCategoryUseCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldBeOK() {

        final var aCategoryID = CategoryID.from(UUID.randomUUID());

        doNothing().when(categoryGateway).deleteById(aCategoryID);

        assertDoesNotThrow(() -> defaultDeleteCategoryUseCase.execute(aCategoryID.getValue()));

        verify(categoryGateway, times(1)).deleteById(aCategoryID);
    }

    @Test
    void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        final var aCategoryID = CategoryID.from("123");

        doNothing().when(categoryGateway).deleteById(aCategoryID);

        assertDoesNotThrow(() -> defaultDeleteCategoryUseCase.execute(aCategoryID.getValue()));

        verify(categoryGateway, times(1)).deleteById(aCategoryID);
    }

    @Test
    void givenAInvalidId_whenGatewayThrowsException_shouldReturnException() {

        final var aCategoryID = CategoryID.from("123");

        doThrow(new IllegalStateException("Gateway error"))
            .when(categoryGateway).deleteById(aCategoryID);

        assertThrows(IllegalStateException.class, () -> defaultDeleteCategoryUseCase.execute(aCategoryID.getValue()));

        verify(categoryGateway, times(1)).deleteById(aCategoryID);
    }

}
