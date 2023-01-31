package com.github.pedrobacchini.admin.catalog.application.category.retrieve.get;

import com.github.pedrobacchini.admin.catalog.DummyUtil;
import com.github.pedrobacchini.admin.catalog.IntegrationTest;
import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryGateway;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;
import com.github.pedrobacchini.admin.catalog.domain.exception.DomainException;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private DefaultGetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidId_whenCallsGetCategory_shouldReturnCategory() {
        final var expectedCategory = DummyUtil.dummyObject(Category.class);
        final var expectedID = expectedCategory.getId();

        save(expectedCategory);

        assertEquals(1, categoryRepository.count());
        final var actualCategory = useCase.execute(expectedID.getValue());

        assertEquals(expectedCategory.getId(), actualCategory.id());
        assertEquals(expectedCategory.getName(), actualCategory.name());
        assertEquals(expectedCategory.getDescription(), actualCategory.description());
        assertEquals(expectedCategory.isActive(), actualCategory.isActive());
        assertEquals(expectedCategory.getType(), actualCategory.type());
        assertEquals(expectedCategory.getCreatedAt(), actualCategory.createdAt());
        assertEquals(expectedCategory.getUpdatedAt(), actualCategory.updatedAt());
        assertEquals(expectedCategory.getDeletedAt(), actualCategory.deletedAt());
    }

    @Test
    void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var aCategoryID = CategoryID.from("123");

        final var actualException = assertThrows(
            DomainException.class,
            () -> useCase.execute(aCategoryID.getValue()));

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
            () -> useCase.execute(aCategoryID.getValue()));

        assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void save(final Category... aCategory) {
        categoryRepository.saveAllAndFlush(Stream.of(aCategory).map(CategoryJpaEntity::from).toList());
    }
}
