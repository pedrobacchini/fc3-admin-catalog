package com.github.pedrobacchini.admin.catalog.infrastructure.category;

import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;
import com.github.pedrobacchini.admin.catalog.domain.category.CategorySearchQuery;
import com.github.pedrobacchini.admin.catalog.domain.category.CommonCategory;
import com.github.pedrobacchini.admin.catalog.infrastructure.MySQLGatewayTest;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MySQLGatewayTest
class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = CommonCategory.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.create(aCategory);

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(actualCategory.getId().getValue())
            .orElseThrow(AssertionFailedError::new);

        assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertEquals(expectedIsActive, actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAValidCategory_whenCallsUpdate_shouldReturnANewCategory() {
        final var aCategory = CommonCategory.newCategory("Film", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.save(CategoryJpaEntity.from(aCategory));

        final var actualInvalidEntity = categoryRepository.findById(aCategory.getId().getValue())
            .orElseThrow(AssertionFailedError::new);

        assertEquals("Film", actualInvalidEntity.getName());
        assertNull(actualInvalidEntity.getDescription());
        assertTrue(actualInvalidEntity.isActive());

        assertEquals(1, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aUpdatedCategory = aCategory.clone().update(expectedName, expectedDescription);

        final var actualCategory = categoryMySQLGateway.update(aUpdatedCategory);

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());

        final var actualEntity = categoryRepository.findById(actualCategory.getId().getValue())
            .orElseThrow(AssertionFailedError::new);

        assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedDescription, actualEntity.getDescription());
        assertEquals(expectedIsActive, actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAPrePesistedCategoryAnValidCategoryId_whenCallsDelete_shouldDeleteCategory() {

        final var aCategory = CommonCategory.newCategory("Filmes", "A categoria", true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.save(CategoryJpaEntity.from(aCategory));

        assertEquals(1, categoryRepository.count());

        categoryMySQLGateway.deleteById(aCategory.getId());

        assertEquals(0, categoryRepository.count());
    }

    @Test
    void givenAPrePesistedCategoryAnInvalidCategoryId_whenCallsDelete_shouldDeleteCategory() {
        final var expectedMessageError = "No class com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity " +
            "entity with id invalid exists!";

        assertEquals(0, categoryRepository.count());

        final var actualException = assertThrows(EmptyResultDataAccessException.class,
            () -> categoryMySQLGateway.deleteById(CategoryID.from("invalid")));

        assertEquals(0, categoryRepository.count());
        assertEquals(expectedMessageError, actualException.getMessage());
    }

    @Test
    void givenAPrePesistedCategoryAnValidCategoryId_whenCallsFindById_shouldReturnCategory() {
        final var aCategory = CommonCategory.newCategory("Filmes", "A categoria", true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.save(CategoryJpaEntity.from(aCategory));

        assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryMySQLGateway.findById(aCategory.getId())
            .orElseThrow(AssertionFailedError::new);

        assertEquals(1, categoryRepository.count());

        assertEquals(aCategory.getId(), actualCategory.getId());
        assertEquals(aCategory.getName(), actualCategory.getName());
        assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        assertEquals(aCategory.isActive(), actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenValidCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty() {

        assertEquals(0, categoryRepository.count());

        final var actualCategoryOptional = categoryMySQLGateway.findById(CategoryID.from("empty"));

        assertEquals(0, categoryRepository.count());

        assertTrue(actualCategoryOptional.isEmpty());
    }

    @Test
    void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = CommonCategory.newCategory("Filmes", null, true);
        final var series = CommonCategory.newCategory("Series", null, true);
        final var documentario = CommonCategory.newCategory("Documentario", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentario)));

        assertEquals(3, categoryRepository.count());

        final var aQuery = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentario.getName(), actualResult.items().get(0).getName());
    }

    @Test
    void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        final var aQuery = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(0, actualResult.items().size());
    }

    @Test
    void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnEmptyPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes = CommonCategory.newCategory("Filmes", null, true);
        final var series = CommonCategory.newCategory("Series", null, true);
        final var documentario = CommonCategory.newCategory("Documentario", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentario)));

        assertEquals(3, categoryRepository.count());

        var aQuery = new CategorySearchQuery(0, 1, "", "name", "asc");
        var actualResult = categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentario.getName(), actualResult.items().get(0).getName());

//        Page 1
        expectedPage = 1;

        aQuery = new CategorySearchQuery(1, 1, "", "name", "asc");
        actualResult = categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(filmes.getName(), actualResult.items().get(0).getName());

//        Page 1
        expectedPage = 2;

        aQuery = new CategorySearchQuery(2, 1, "", "name", "asc");
        actualResult = categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(series.getName(), actualResult.items().get(0).getName());
    }

    @Test
    void givenPrePersistedCategoriesAndDocAsTerm_whenCallsFindAllAndTermsMatchsCategoryName_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = CommonCategory.newCategory("Filmes", null, true);
        final var series = CommonCategory.newCategory("Series", null, true);
        final var documentario = CommonCategory.newCategory("Documentario", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentario)));

        assertEquals(3, categoryRepository.count());

        final var aQuery = new CategorySearchQuery(0, 1, "doc", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentario.getName(), actualResult.items().get(0).getName());
    }

    @Test
    void givenPrePersistedCategoriesAndMaisAssistidaAsTerm_whenCallsFindAllAndTermsMatchsCategoryDescription_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes = CommonCategory.newCategory("Filmes", "A categoria mais assistida", true);
        final var series = CommonCategory.newCategory("Series", "Uma categoria assistida", true);
        final var documentario = CommonCategory.newCategory("Documentario", "A categoria menos assistida", true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
            CategoryJpaEntity.from(filmes),
            CategoryJpaEntity.from(series),
            CategoryJpaEntity.from(documentario)));

        assertEquals(3, categoryRepository.count());

        final var aQuery = new CategorySearchQuery(0, 1, "MAIS ASSISTIDA", "name", "asc");
        final var actualResult = categoryMySQLGateway.findAll(aQuery);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(filmes.getName(), actualResult.items().get(0).getName());
    }

}