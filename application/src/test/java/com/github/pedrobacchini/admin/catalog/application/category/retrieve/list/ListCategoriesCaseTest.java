package com.github.pedrobacchini.admin.catalog.application.category.retrieve.list;

import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryGateway;
import com.github.pedrobacchini.admin.catalog.domain.category.CategorySearchQuery;
import com.github.pedrobacchini.admin.catalog.domain.pagination.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.github.pedrobacchini.admin.catalog.application.DummyUtil.dummyObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase defaultListCategoriesUseCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

//    @Test
//    void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategorries() {
//
//        final var aQuery = dummyObject(CategorySearchQuery.class);
//        final var categories = dummyObjects(CommonCategory.class, 10);
//        final var expectedPagination = new Pagination<>(aQuery.page(), aQuery.perPage(), categories.size(), categories);
//        final var expectedResult = expectedPagination.map(CaregoryListOutput::from);
//
//        when(categoryGateway.findAll(aQuery)).thenReturn(expectedPagination);
//
//        final var actualResult = defaultListCategoriesUseCase.execute(aQuery);
//
//        verify(categoryGateway, times(1)).findAll(aQuery);
//
//        assertNotNull(actualResult);
//        assertEquals(expectedPagination.items().size(), actualResult.items().size());
//        assertEquals(expectedResult, actualResult);
//        assertEquals(aQuery.page(), actualResult.currentPage());
//        assertEquals(aQuery.perPage(), actualResult.perPage());
//        assertEquals(categories.size(), actualResult.total());
//    }

    @Test
    void givenAInvalidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {
        final var aQuery = dummyObject(CategorySearchQuery.class);
        final var pagination = new Pagination<Category>(aQuery.page(), aQuery.perPage(), 0, List.of());

        when(categoryGateway.findAll(aQuery)).thenReturn(pagination);

        final var actualResult = defaultListCategoriesUseCase.execute(aQuery);

        verify(categoryGateway, times(1)).findAll(aQuery);

        assertNotNull(actualResult);
        assertEquals(0, actualResult.items().size());
        assertEquals(aQuery.page(), actualResult.currentPage());
        assertEquals(aQuery.perPage(), actualResult.perPage());
        assertEquals(0, actualResult.total());
    }

    @Test
    void givenAInvalidQuery_whenGatewayThrowsException_shouldReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var aQuery = dummyObject(CategorySearchQuery.class);

        when(categoryGateway.findAll(aQuery)).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = assertThrows(
            IllegalStateException.class,
            () -> defaultListCategoriesUseCase.execute(aQuery));

        verify(categoryGateway, times(1)).findAll(aQuery);

        assertEquals(expectedErrorMessage, actualException.getMessage());

    }

}
