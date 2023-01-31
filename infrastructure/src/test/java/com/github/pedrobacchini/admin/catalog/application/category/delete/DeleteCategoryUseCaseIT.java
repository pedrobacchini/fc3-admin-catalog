package com.github.pedrobacchini.admin.catalog.application.category.delete;

import com.github.pedrobacchini.admin.catalog.DummyUtil;
import com.github.pedrobacchini.admin.catalog.IntegrationTest;
import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryGateway;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DefaultDeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldBeOK() {
        final var expectedCategory = DummyUtil.dummyObject(Category.class);
        final var aCategoryID = expectedCategory.getId();

        save(expectedCategory);

        assertEquals(1, categoryRepository.count());

        assertDoesNotThrow(() -> useCase.execute(aCategoryID.getValue()));

        assertEquals(0, categoryRepository.count());
    }

    @Test
    void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK() {
        final var expectedMessageError = "No class com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity " +
            "entity with id invalid exists";

        assertEquals(0, categoryRepository.count());

        final var actualException = assertThrows(EmptyResultDataAccessException.class,
            () -> useCase.execute(CategoryID.from("invalid").getValue()));

        assertEquals(0, categoryRepository.count());
        assertEquals(expectedMessageError, actualException.getMessage());
    }

    @Test
    void givenAValidId_whenGatewayThrowsException_shouldReturnException() {

        final var aCategoryID = CategoryID.from(UUID.randomUUID());

        doThrow(new IllegalStateException("Gateway error"))
            .when(categoryGateway).deleteById(aCategoryID);

        assertThrows(IllegalStateException.class, () -> useCase.execute(aCategoryID.getValue()));
    }

    private void save(final Category... aCategory) {
        categoryRepository.saveAllAndFlush(Stream.of(aCategory).map(CategoryJpaEntity::from).toList());
    }

}
