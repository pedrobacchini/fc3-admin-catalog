package com.github.pedrobacchini.admin.catalog.application.category.update;

import com.github.pedrobacchini.admin.catalog.domain.category.CategoryGateway;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;
import com.github.pedrobacchini.admin.catalog.domain.category.CommonCategory;
import com.github.pedrobacchini.admin.catalog.domain.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = CommonCategory.newCategory("Film", null, true);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(
            argThat(aUpdatedCategory -> Objects.equals(expectedId, aUpdatedCategory.getId())
                && Objects.equals(expectedName, aUpdatedCategory.getName())
                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                && Objects.isNull(aUpdatedCategory.getDeletedAt())));
    }

    @Test
    void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory = CommonCategory.newCategory("Film", null, true);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive);
        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertTrue(notification.firstError().isPresent());
        assertEquals(expectedErrorMessage, notification.firstError().get().message());

        verify(categoryGateway, never()).update(any());
    }

    @Test
    void givenAValidInactiveCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategory = CommonCategory.newCategory("Film", null, true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1))
            .update(argThat(aUpdatedCategory -> Objects.equals(expectedId, aUpdatedCategory.getId())
                && Objects.equals(expectedName, aUpdatedCategory.getName())
                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                && Objects.nonNull(aUpdatedCategory.getCreatedAt())
                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                && Objects.nonNull(aUpdatedCategory.getDeletedAt())));
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var aCategory = CommonCategory.newCategory("Film", null, true);
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive);

        when(categoryGateway.findById(eq(expectedId))).thenReturn(Optional.of(aCategory.clone()));
        when(categoryGateway.update(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertTrue(notification.firstError().isPresent());
        assertEquals(expectedErrorMessage, notification.firstError().get().message());

        verify(categoryGateway, times(1))
            .update(argThat(aUpdatedCategory -> Objects.nonNull(aUpdatedCategory.getId())
                && Objects.equals(expectedName, aUpdatedCategory.getName())
                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                && Objects.nonNull(aUpdatedCategory.getCreatedAt())
                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                && Objects.isNull(aUpdatedCategory.getDeletedAt())));
    }

    @Test
    void givenACommandWithInvalidId_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = "123";
        final var expectedErrorErrorCount = 1;
        final var expectedErrorMessage = "Category with ID %s was not found".formatted(expectedId);

        final var aCommand = UpdateCategoryCommand.with(
            expectedId,
            expectedName,
            expectedDescription,
            expectedIsActive);

        when(categoryGateway.findById(eq(CategoryID.from(expectedId)))).thenReturn(Optional.empty());

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(expectedErrorErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId)));
        verify(categoryGateway, never()).update(any());
    }

}
