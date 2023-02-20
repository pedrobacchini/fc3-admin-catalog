package com.github.pedrobacchini.admin.catalog.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pedrobacchini.admin.catalog.ControllerTest;
import com.github.pedrobacchini.admin.catalog.DummyUtil;
import com.github.pedrobacchini.admin.catalog.application.category.create.CreateCategoryOutput;
import com.github.pedrobacchini.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;
import com.github.pedrobacchini.admin.catalog.domain.exception.DomainException;
import com.github.pedrobacchini.admin.catalog.domain.exception.NotFoundException;
import com.github.pedrobacchini.admin.catalog.domain.validation.Error;
import com.github.pedrobacchini.admin.catalog.domain.validation.handler.Notification;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.CreateCategoryApiInput;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        // given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedType = CategoryType.COMMON;

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive, expectedType);

        when(createCategoryUseCase.execute(any()))
            .thenReturn(Right(CreateCategoryOutput.from("123")));

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(aInput));
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
        final var expectedMessageError = "'name' should not be null";

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive, expectedType);

        when(createCategoryUseCase.execute(any()))
            .thenReturn(Left(Notification.create(new Error(expectedMessageError))));

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(aInput));

        final var response = mockMvc.perform(request)
            .andDo(print());

        // then
        response
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Location", nullValue()))
            .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors", hasSize(1)))
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

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive, expectedType);

        when(createCategoryUseCase.execute(any()))
            .thenThrow(DomainException.with(new Error(expectedMessageError)));

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(aInput));

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
        final var expectedCategory = DummyUtil.dummyObject(Category.class);
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
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

}
