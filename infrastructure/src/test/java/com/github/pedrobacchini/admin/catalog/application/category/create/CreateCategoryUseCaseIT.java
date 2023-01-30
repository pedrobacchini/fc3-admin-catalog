package com.github.pedrobacchini.admin.catalog.application.category.create;

import com.github.pedrobacchini.admin.catalog.IntegrationTest;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryGateway;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private DefaultCreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedType = CategoryType.COMMON;

        assertEquals(0, categoryRepository.count());

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive, expectedType);

        final var actualOutput = useCase.execute(aCommand).get();

        assertEquals(1, categoryRepository.count());

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var aEntity = categoryRepository.findById(actualOutput.id().getValue())
            .orElseThrow(AssertionFailedError::new);

        assertEquals(expectedName, aEntity.getName());
        assertEquals(expectedDescription, aEntity.getDescription());
        assertEquals(expectedIsActive, aEntity.isActive());
        assertEquals(expectedType, aEntity.getType());
        assertNotNull(aEntity.getCreatedAt());
        assertNotNull(aEntity.getUpdatedAt());
        assertNull(aEntity.getDeletedAt());
    }

    @Test
    void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedType = CategoryType.COMMON;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        assertEquals(0, categoryRepository.count());

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive, expectedType);

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(0, categoryRepository.count());

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertTrue(notification.firstError().isPresent());
        assertEquals(expectedErrorMessage, notification.firstError().get().message());

        verify(categoryGateway, never()).create(any());
    }

    @Test
    void givenAValidCommandWithInactiveCategoryCommon_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedType = CategoryType.COMMON;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive, expectedType);

        assertEquals(0, categoryRepository.count());

        final var actualOutput = useCase.execute(aCommand).get();

        assertEquals(1, categoryRepository.count());

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        final var aEntity = categoryRepository.findById(actualOutput.id().getValue())
            .orElseThrow(AssertionFailedError::new);

        assertEquals(expectedName, aEntity.getName());
        assertEquals(expectedDescription, aEntity.getDescription());
        assertEquals(expectedIsActive, aEntity.isActive());
        assertEquals(expectedType, aEntity.getType());
        assertNotNull(aEntity.getCreatedAt());
        assertNotNull(aEntity.getUpdatedAt());
        assertNotNull(aEntity.getDeletedAt());
    }

    @Test
    void givenAInvalidCommandWithInactiveCategoryRestrict_whenCallsCreateCategory_shouldReturnDomainException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedType = CategoryType.RESTRICT;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'active' cannot be false for restrict category";

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive, expectedType);

        assertEquals(0, categoryRepository.count());

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(0, categoryRepository.count());

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertTrue(notification.firstError().isPresent());
        assertEquals(expectedErrorMessage, notification.firstError().get().message());

        verify(categoryGateway, never()).create(any());
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedType = CategoryType.COMMON;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive, expectedType);

        assertEquals(0, categoryRepository.count());

        doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).create(any());

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(0, categoryRepository.count());

        assertEquals(expectedErrorCount, notification.getErrors().size());
        assertTrue(notification.firstError().isPresent());
        assertEquals(expectedErrorMessage, notification.firstError().get().message());
    }

}
