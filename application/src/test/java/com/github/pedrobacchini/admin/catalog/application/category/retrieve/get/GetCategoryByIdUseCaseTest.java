package com.github.pedrobacchini.admin.catalog.application.category.retrieve.get;

import com.github.pedrobacchini.admin.catalog.application.DummyUtil;
import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryGateway;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;
import com.github.pedrobacchini.admin.catalog.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase defaultGetCategoryByIdUseCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedCategory = DummyUtil.dummyObject(Category.class);
        final var expectedID = expectedCategory.getId();

        when(categoryGateway.findById(expectedID)).thenReturn(Optional.of(expectedCategory.clone()));

        final var actualCategory = defaultGetCategoryByIdUseCase.execute(expectedID.getValue());

        verify(categoryGateway, times(1)).findById(expectedID);

        assertEquals(expectedCategory.getID(), actualCategory.id());
        assertEquals(expectedCategory.getName(), actualCategory.name());
        assertEquals(expectedCategory.getDescription(), actualCategory.description());
        assertEquals(expectedCategory.isActive(), actualCategory.isActive());
        assertEquals(expectedCategory.getCreatedAt(), actualCategory.createdAt());
        assertEquals(expectedCategory.getUpdatedAt(), actualCategory.updatedAt());
        assertEquals(expectedCategory.getDeletedAt(), actualCategory.deletedAt());
    }

    @Test
    void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var aCategoryID = CategoryID.from("123");

        when(categoryGateway.findById(aCategoryID)).thenReturn(Optional.empty());

        final var actualException = assertThrows(
            DomainException.class,
            () -> defaultGetCategoryByIdUseCase.execute(aCategoryID.getValue()));

        verify(categoryGateway, times(1)).findById(aCategoryID);

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAInvalidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var aCategoryID = CategoryID.from("123");

        doThrow(new IllegalStateException(expectedErrorMessage))
            .when(categoryGateway).findById(aCategoryID);

        final var actualException = assertThrows(
            IllegalStateException.class,
            () -> defaultGetCategoryByIdUseCase.execute(aCategoryID.getValue()));

        verify(categoryGateway, times(1)).findById(aCategoryID);

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

}
