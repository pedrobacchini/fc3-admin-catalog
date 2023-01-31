package com.github.pedrobacchini.admin.catalog.application.category.retrieve.list;

import com.github.pedrobacchini.admin.catalog.IntegrationTest;
import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategorySearchQuery;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class ListCategoriesCaseIT {

    @Autowired
    private DefaultListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void mockUp() {
        final var categories = Stream.of(
                Category.newCategory("Filmes", null, true, CategoryType.COMMON),
                Category.newCategory("Netflix", "Titulos de autoria da Netflix", true, CategoryType.COMMON),
                Category.newCategory("Amazon", "Titulos de autoria da Amazon", true, CategoryType.COMMON),
                Category.newCategory("Documentarios", null, true, CategoryType.COMMON),
                Category.newCategory("Sports", null, true, CategoryType.COMMON),
                Category.newCategory("Kids", "Categoria para crianças", true, CategoryType.COMMON),
                Category.newCategory("Series", null, true, CategoryType.COMMON))
            .map(CategoryJpaEntity::from)
            .toList();
        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    void givenAValidTermWhenTermDoesntMatchsPrepersisted_shouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "djaskjdklas";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
        "fil,0,10,1,1,Filmes",
        "net,0,10,1,1,Netflix",
        "ZON,0,10,1,1,Amazon",
        "KI,0,10,1,1,Kids",
        "crianças,0,10,1,1,Kids",
        "da Amazon, 0,10,1,1, Amazon"
    })
    void givenAValidTerm_whenCallsListCategories_shouldReturnCategoriesFiltered(
        final String expectedTerms,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final long expectedTotal,
        final String expectedCategoryName) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
        "name,asc,0,10,7,7,Amazon",
        "name,desc,0,10,7,7,Sports",
        "createdAt,asc,0,10,7,7,Filmes",
        "createdAt,desc,0,10,7,7,Series",
    })
    void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(
        final String expectedSort,
        final String expectedDirection,
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final long expectedTotal,
        final String expectedCategoryName) {
        final var expectedTerms = "";

        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
        "0,2,2,7,Amazon;Documentarios",
        "1,2,2,7,Filmes;Kids",
        "2,2,2,7,Netflix;Series",
        "3,2,1,7,Sports",
    })
    void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(
        final int expectedPage,
        final int expectedPerPage,
        final int expectedItemsCount,
        final long expectedTotal,
        final String expectedCategoriesName) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (String expectedName : expectedCategoriesName.split(";")) {
            final var actualName = actualResult.items().get(index).name();
            assertEquals(expectedName, actualName);
            index++;
        }
    }

}
