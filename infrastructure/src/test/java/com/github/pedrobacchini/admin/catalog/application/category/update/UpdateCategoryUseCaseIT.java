package com.github.pedrobacchini.admin.catalog.application.category.update;

import com.github.pedrobacchini.admin.catalog.IntegrationTest;
import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryGateway;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;
import com.github.pedrobacchini.admin.catalog.domain.exception.DomainException;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private DefaultUpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory = Category.newCategory("Film", null, true, CategoryType.COMMON);

        save(aCategory);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedType = CategoryType.COMMON;

        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive);

        assertEquals(1, categoryRepository.count());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var aEntity = categoryRepository.findById(expectedId.getValue())
            .orElseThrow(AssertionFailedError::new);

        assertEquals(expectedName, aEntity.getName());
        assertEquals(expectedDescription, aEntity.getDescription());
        assertEquals(expectedIsActive, aEntity.isActive());
        assertEquals(expectedType, aEntity.getType());
        assertEquals(aCategory.getCreatedAt(), aEntity.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(aEntity.getUpdatedAt()));
        assertNull(aEntity.getDeletedAt());
    }

    @Test
    void givenAInvalidName_whenCallsUpdateCategory_shouldReturnDomainException() {
        final var aCategory = Category.newCategory("Film", null, true, CategoryType.COMMON);

        save(aCategory);

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

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertTrue(notification.firstError().isPresent());
        assertEquals(expectedErrorMessage, notification.firstError().get().message());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, never()).update(any());
    }

    @Test
    void givenAValidInactiveCommand_whenCallsUpdateCategoryCommon_shouldReturnInactiveCategoryId() {
        final var aCategory = Category.newCategory("Filme", null, true, CategoryType.COMMON);

        save(aCategory);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedType = CategoryType.COMMON;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            expectedIsActive);

        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var aEntity = categoryRepository.findById(expectedId.getValue())
            .orElseThrow(AssertionFailedError::new);

        assertEquals(expectedName, aEntity.getName());
        assertEquals(expectedDescription, aEntity.getDescription());
        assertEquals(expectedIsActive, aEntity.isActive());
        assertEquals(expectedType, aEntity.getType());
        assertEquals(aCategory.getCreatedAt(), aEntity.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(aEntity.getUpdatedAt()));
        assertNotNull(aEntity.getDeletedAt());
    }

    @Test
    void givenAInvalidInactiveCommand_whenCallsUpdateCategoryRestrict_shouldReturnAException() {
        final var aCategory = Category.newCategory("Filme", "A categoria mais assistida", true, CategoryType.RESTRICT);

        save(aCategory);

        final var expectedId = aCategory.getId();
        final var expectedName = aCategory.getName();
        final var expectedDescription = aCategory.getDescription();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'active' cannot be false for restrict category";

        final var aCommand = UpdateCategoryCommand.with(
            expectedId.getValue(),
            expectedName,
            expectedDescription,
            false);

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertTrue(notification.firstError().isPresent());
        assertEquals(expectedErrorMessage, notification.firstError().get().message());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, never()).update(any());
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var aCategory = Category.newCategory("Film", null, true, CategoryType.COMMON);

        save(aCategory);

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

        doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).update(any());

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertTrue(notification.firstError().isPresent());
        assertEquals(expectedErrorMessage, notification.firstError().get().message());

        final var aEntity = categoryRepository.findById(expectedId.getValue())
            .orElseThrow(AssertionFailedError::new);

        assertEquals(aCategory.getName(), aEntity.getName());
        assertEquals(aCategory.getDescription(), aEntity.getDescription());
        assertEquals(aCategory.isActive(), aEntity.isActive());
        assertEquals(aCategory.getType(), aEntity.getType());
        assertEquals(aCategory.getCreatedAt(), aEntity.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), aEntity.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), aEntity.getDeletedAt());
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

        final var actualException = assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(expectedErrorErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void save(final Category... aCategory) {
        categoryRepository.saveAllAndFlush(Stream.of(aCategory).map(CategoryJpaEntity::from).toList());
    }

}
