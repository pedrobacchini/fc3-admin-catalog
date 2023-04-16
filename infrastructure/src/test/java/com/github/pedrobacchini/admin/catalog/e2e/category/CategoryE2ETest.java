package com.github.pedrobacchini.admin.catalog.e2e.category;

import com.github.pedrobacchini.admin.catalog.E2ETest;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.CategoryResponse;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.CreateCategoryRequest;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.UpdateCategoryRequest;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import com.github.pedrobacchini.admin.catalog.infrastructure.configuration.json.Json;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    private static final MySQLContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:latest")
        .withPassword("123456")
        .withUsername("root")
        .withDatabaseName("adm_videos");

    @DynamicPropertySource
    private static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var mappedPort = MY_SQL_CONTAINER.getMappedPort(3306);
        System.out.println("Container is running on port: " + mappedPort);
        registry.add("mysql.port", () -> mappedPort);
    }

    @Test
    void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedType = CategoryType.COMMON;

        final var actualId = givenAValidCategory(expectedName, expectedDescription, expectedIsActive, expectedType);

        final var actualCategory = categoryRepository.findById(actualId.getValue())
            .orElseThrow(AssertionFailedError::new);

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(expectedType, actualCategory.getType());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIsShouldBeAbleToNavigateToAllCategories() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        givenAValidCategory("Filmes", null, true, CategoryType.COMMON);
        givenAValidCategory("Documentarios", null, true, CategoryType.COMMON);
        givenAValidCategory("Series", null, true, CategoryType.COMMON);

        listCategories(0, 1)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(0)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize(1)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Documentarios")));

        listCategories(1, 1)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(1)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize(1)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")));

        listCategories(2, 1)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(2)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize(1)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Series")));

        listCategories(3, 1)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(3)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void asACatalogAdminIsShouldBeAbleToSearchBetweenAllCategories() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        givenAValidCategory("Filmes", null, true, CategoryType.COMMON);
        givenAValidCategory("Documentarios", null, true, CategoryType.COMMON);
        givenAValidCategory("Series", null, true, CategoryType.COMMON);

        listCategories(0, 1, "fil")
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(0)))
            .andExpect(jsonPath("$.per_page", equalTo(1)))
            .andExpect(jsonPath("$.total", equalTo(1)))
            .andExpect(jsonPath("$.items", hasSize(1)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")));
    }

    @Test
    public void asACatalogAdminIsShouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        givenAValidCategory("Filmes", "C", true, CategoryType.COMMON);
        givenAValidCategory("Documentarios", "Z", true, CategoryType.COMMON);
        givenAValidCategory("Series", "A", true, CategoryType.COMMON);

        listCategories(0, 3, "", "description", "desc")
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(0)))
            .andExpect(jsonPath("$.per_page", equalTo(3)))
            .andExpect(jsonPath("$.total", equalTo(3)))
            .andExpect(jsonPath("$.items", hasSize(3)))
            .andExpect(jsonPath("$.items[0].name", equalTo("Documentarios")))
            .andExpect(jsonPath("$.items[1].name", equalTo("Filmes")))
            .andExpect(jsonPath("$.items[2].name", equalTo("Series")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToGetACategoryByItsIdentifier() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedType = CategoryType.COMMON;

        final var actualId = givenAValidCategory(expectedName, expectedDescription, expectedIsActive, expectedType);

        final var actualCategory = retrieveACategory(actualId.getValue());

        assertEquals(expectedName, actualCategory.name());
        assertEquals(expectedDescription, actualCategory.description());
        assertEquals(expectedIsActive, actualCategory.active());
        assertEquals(expectedType, actualCategory.type());
        assertNotNull(actualCategory.createdAt());
        assertNotNull(actualCategory.updatedAt());
        assertNull(actualCategory.deletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        final var request = get("/categories/123")
            .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message", equalTo("Category with ID 123 was not found")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        final var actualId = givenAValidCategory("Movies", null, true, CategoryType.COMMON);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = put("/categories/" + actualId.getValue())
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(requestBody));
        mockMvc.perform(request)
            .andDo(print())
            .andExpect(status().isOk());

        final var actualCategory = categoryRepository.findById(actualId.getValue())
            .orElseThrow(AssertionFailedError::new);

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(CategoryType.COMMON, actualCategory.getType());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToInactivateACategoryByItsIdentifier() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var actualId = givenAValidCategory(expectedName, expectedDescription, true, CategoryType.COMMON);

        final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = put("/categories/" + actualId.getValue())
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(requestBody));
        mockMvc.perform(request)
            .andDo(print())
            .andExpect(status().isOk());

        final var actualCategory = categoryRepository.findById(actualId.getValue())
            .orElseThrow(AssertionFailedError::new);

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(CategoryType.COMMON, actualCategory.getType());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToActivateACategoryByItsIdentifier() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var actualId = givenAValidCategory(expectedName, expectedDescription, false, CategoryType.COMMON);

        final var requestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final var request = put("/categories/" + actualId.getValue())
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(requestBody));
        mockMvc.perform(request)
            .andDo(print())
            .andExpect(status().isOk());

        final var actualCategory = categoryRepository.findById(actualId.getValue())
            .orElseThrow(AssertionFailedError::new);

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(CategoryType.COMMON, actualCategory.getType());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() throws Exception {
        assertTrue(MY_SQL_CONTAINER.isRunning());

        assertEquals(0, categoryRepository.count());

        final var actualId = givenAValidCategory("Filmes", null, true, CategoryType.COMMON);

        final var request = delete("/categories/" + actualId.getValue());

        mockMvc.perform(request)
            .andDo(print())
            .andExpect(status().isNoContent());

        assertFalse(categoryRepository.existsById(actualId.getValue()));
    }

    private CategoryID givenAValidCategory(
        final String aName,
        final String aDescription,
        final boolean isActive,
        final CategoryType aType) throws Exception {
        final var requestBody = new CreateCategoryRequest(aName, aDescription, isActive, aType);
        final var request = post("/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(Json.writeValueAsString(requestBody));
        final var actualId = mockMvc.perform(request)
            .andDo(print())
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location")
            .replace("/categories/", "");

        return CategoryID.from(actualId);
    }

    private CategoryResponse retrieveACategory(final String anId) throws Exception {
        final var request = get("/categories/" + anId)
            .accept(MediaType.APPLICATION_JSON);

        final var json = mockMvc.perform(request)
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn()
            .getResponse().getContentAsString();

        return Json.readValue(json, CategoryResponse.class);
    }

    private ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    private ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return listCategories(page, perPage, search, "", "");
    }

    private ResultActions listCategories(
        final int page,
        final int perPage,
        final String search,
        final String sort,
        final String dir) throws Exception {
        final var request = get("/categories")
            .queryParam("page", String.valueOf(page))
            .queryParam("perPage", String.valueOf(perPage))
            .queryParam("search", search)
            .queryParam("sort", sort)
            .queryParam("dir", dir)
            .accept(MediaType.APPLICATION_JSON);

        return mockMvc.perform(request);
    }

}

