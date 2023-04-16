package com.github.pedrobacchini.admin.catalog.infrastructure.api;

import com.github.pedrobacchini.admin.catalog.ControllerTest;
import com.github.pedrobacchini.admin.catalog.application.category.create.CreateCategoryOutput;
import com.github.pedrobacchini.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.list.CaregoryListOutput;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.update.UpdateCategoryOutput;
import com.github.pedrobacchini.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;
import com.github.pedrobacchini.admin.catalog.domain.exception.DomainException;
import com.github.pedrobacchini.admin.catalog.domain.exception.NotFoundException;
import com.github.pedrobacchini.admin.catalog.domain.pagination.Pagination;
import com.github.pedrobacchini.admin.catalog.domain.validation.Error;
import com.github.pedrobacchini.admin.catalog.domain.validation.handler.Notification;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.CreateCategoryRequest;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.UpdateCategoryRequest;
import com.github.pedrobacchini.admin.catalog.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static com.github.pedrobacchini.admin.catalog.DummyUtil.dummyObject;
import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        // given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedType = CategoryType.COMMON;

        final var aInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive, expectedType);

        when(createCategoryUseCase.execute(any()))
            .thenReturn(Right(CreateCategoryOutput.from("123")));

        // when
        final var request = post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(aInput));
        final var response = mockMvc.perform(request)
            .andDo(print());

        // then
        response
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/categories/123"))
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name()) &&
                Objects.equals(expectedDescription, cmd.description()) &&
                Objects.equals(expectedIsActive, cmd.isActive()) &&
                Objects.equals(expectedType, cmd.type())));
    }

    @Test
    void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedType = CategoryType.COMMON;

        final var expectedErrorCount = 1;
        final var expectedMessageError = "'name' should not be null";

        final var aInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive, expectedType);

        when(createCategoryUseCase.execute(any()))
            .thenReturn(Left(Notification.create(new Error(expectedMessageError))));

        // when
        final var request = post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(aInput));

        final var response = mockMvc.perform(request)
            .andDo(print());

        // then
        response
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Location", nullValue()))
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessageError)));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name()) &&
                Objects.equals(expectedDescription, cmd.description()) &&
                Objects.equals(expectedIsActive, cmd.isActive()) &&
                Objects.equals(expectedType, cmd.type())));
    }

    @Test
    void givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedType = CategoryType.COMMON;
        final var expectedMessageError = "'name' should not be null";

        final var aInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive, expectedType);

        when(createCategoryUseCase.execute(any()))
            .thenThrow(DomainException.with(new Error(expectedMessageError)));

        // when
        final var request = post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(aInput));

        final var response = mockMvc.perform(request)
            .andDo(print());

        // then
        response
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Location", nullValue()))
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedMessageError)))
            .andExpect(jsonPath("$.errors", hasSize(1)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessageError)));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name()) &&
                Objects.equals(expectedDescription, cmd.description()) &&
                Objects.equals(expectedIsActive, cmd.isActive()) &&
                Objects.equals(expectedType, cmd.type())));
    }

    @Test
    void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception {
        // given
        final var expectedCategory = dummyObject(Category.class);
        final var expectedID = expectedCategory.getId().getValue();

        when(getCategoryByIdUseCase.execute(any()))
            .thenReturn(CategoryOutput.from(expectedCategory));

        // when
        final var request = get("/categories/{id}", expectedID)
            .accept(MediaType.APPLICATION_JSON);

        final var response = this.mockMvc.perform(request)
            .andDo(print());

        // then
        response.andExpect(status().isOk())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedID)))
            .andExpect(jsonPath("$.name", equalTo(expectedCategory.getName())))
            .andExpect(jsonPath("$.description", equalTo(expectedCategory.getDescription())))
            .andExpect(jsonPath("$.is_active", equalTo(expectedCategory.isActive())))
            .andExpect(jsonPath("$.type", equalTo(expectedCategory.getType().name())))
            .andExpect(jsonPath("$.created_at", equalTo(expectedCategory.getCreatedAt().toString())))
            .andExpect(jsonPath("$.updated_at", equalTo(expectedCategory.getUpdatedAt().toString())))
            .andExpect(jsonPath("$.deleted_at", equalTo(expectedCategory.getDeletedAt().toString())));

        verify(getCategoryByIdUseCase, times(1)).execute(expectedID);
    }

    @Test
    void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
        // given
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var aCategoryID = CategoryID.from("123");

        when(getCategoryByIdUseCase.execute(any()))
            .thenThrow(NotFoundException.with(Category.class, aCategoryID));

        // when
        final var request = get("/categories/{id}", aCategoryID.getValue());

        final var response = this.mockMvc.perform(request)
            .andDo(print());

        // then
        response.andExpect(status().isNotFound())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception {
        // given
        final var expectedID = dummyObject(String.class);
        final var aInput = dummyObject(UpdateCategoryRequest.class);

        when(updateCategoryUseCase.execute(any()))
            .thenReturn(Right(UpdateCategoryOutput.from(expectedID)));

        // when
        final var request = put("/categories/{id}", expectedID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(MediaType.APPLICATION_JSON_VALUE)
            .content(Json.writeValueAsString(aInput));

        final var response = this.mockMvc.perform(request)
            .andDo(print());

        // then
        response.andExpect(status().isOk())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedID)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
            Objects.equals(expectedID, cmd.id()) &&
                Objects.equals(aInput.name(), cmd.name()) &&
                Objects.equals(aInput.description(), cmd.description()) &&
                Objects.equals(aInput.active(), cmd.isActive())));
    }

    @Test
    void givenACommandWithInvalidId_whenCallsUpdateCategory_shouldReturnNotFoundException() throws Exception {
        // given
        final var expectedID = "not-found";
        final var expectedErrorMessage = "Category with ID not-found was not found";
        final var aInput = dummyObject(UpdateCategoryRequest.class);

        when(updateCategoryUseCase.execute(any()))
            .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedID)));

        // when
        final var request = put("/categories/{id}", expectedID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(MediaType.APPLICATION_JSON_VALUE)
            .content(Json.writeValueAsString(aInput));

        final var response = this.mockMvc.perform(request)
            .andDo(print());

        // then
        response.andExpect(status().isNotFound())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
            Objects.equals(expectedID, cmd.id()) &&
                Objects.equals(aInput.name(), cmd.name()) &&
                Objects.equals(aInput.description(), cmd.description()) &&
                Objects.equals(aInput.active(), cmd.isActive())));
    }

    @Test
    void givenAInvalidName_whenCallsUpdateCategory_shouldReturnDomainException() throws Exception {
        // given
        final var expectedID = dummyObject(String.class);
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorCount = 1;
        final var expectedMessageError = "'name' should not be null";

        final var aInput = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(updateCategoryUseCase.execute(any()))
            .thenReturn(Left(Notification.create(new Error(expectedMessageError))));

        // when
        final var request = put("/categories/{id}", expectedID)
            .contentType(MediaType.APPLICATION_JSON)
            .content(MediaType.APPLICATION_JSON_VALUE)
            .content(Json.writeValueAsString(aInput));

        final var response = this.mockMvc.perform(request)
            .andDo(print());

        // then
        response
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessageError)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
            Objects.equals(expectedName, cmd.name()) &&
                Objects.equals(expectedDescription, cmd.description()) &&
                Objects.equals(expectedIsActive, cmd.isActive())));
    }

    @Test
    void givenAValidId_whenCallsDeleteCategory_shouldReturnNoContent() throws Exception {
        // given
        final var expectedID = dummyObject(String.class);

        // when
        final var request = delete("/categories/{id}", expectedID)
            .accept(MediaType.APPLICATION_JSON);

        final var response = this.mockMvc.perform(request)
            .andDo(print());

        // then
        response.andExpect(status().isNoContent());

        verify(deleteCategoryUseCase, times(1)).execute(expectedID);
    }

    @Test
    void givenValidParams_whenCallsListCategories_shouldReturnCategories() throws Exception {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "djaskjdklas";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var aCategory = Category.newCategory("Movie", null, true, CategoryType.COMMON);
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, List.of(aCategory));
        final var expectedResult = expectedPagination.map(CaregoryListOutput::from);

        when(listCategoriesUseCase.execute(any()))
            .thenReturn(expectedResult);

        // when
        final var request = get("/categories")
            .queryParam("page", String.valueOf(expectedPage))
            .queryParam("perPage", String.valueOf(expectedPerPage))
            .queryParam("search", expectedTerms)
            .queryParam("sort", expectedSort)
            .queryParam("dir", expectedDirection)
            .accept(MediaType.APPLICATION_JSON);

        final var response = this.mockMvc.perform(request)
            .andDo(print());

        // then
        response.andExpect(status().isOk())
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
            .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
            .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
            .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
            .andExpect(jsonPath("$.items[0].id", equalTo(aCategory.getId().getValue())))
            .andExpect(jsonPath("$.items[0].name", equalTo(aCategory.getName())))
            .andExpect(jsonPath("$.items[0].description", equalTo(aCategory.getDescription())))
            .andExpect(jsonPath("$.items[0].is_active", equalTo(aCategory.isActive())))
            .andExpect(jsonPath("$.items[0].type", equalTo(aCategory.getType().toString())))
            .andExpect(jsonPath("$.items[0].created_at", equalTo(aCategory.getCreatedAt().toString())))
            .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(listCategoriesUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                    && Objects.equals(expectedPerPage, query.perPage())
                    && Objects.equals(expectedDirection, query.direction())
                    && Objects.equals(expectedSort, query.sort())
                    && Objects.equals(expectedTerms, query.terms())));
    }

}
