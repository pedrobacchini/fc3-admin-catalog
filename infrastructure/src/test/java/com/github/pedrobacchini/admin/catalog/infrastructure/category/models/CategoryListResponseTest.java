package com.github.pedrobacchini.admin.catalog.infrastructure.category.models;

import com.github.pedrobacchini.admin.catalog.JacksonTest;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.CategoryListResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JacksonTest
public class CategoryListResponseTest {

    @Autowired
    private JacksonTester<CategoryListResponse> json;

    @Test
    void testMarshall() throws IOException {
        final var expectedId = "1";
        final var expectedName = "Filmes";
        final var expectedDescription = "Filmes de ação";
        final var expectedActive = true;
        final var expectedType = CategoryType.COMMON;
        final var expectedCreatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new CategoryListResponse(
            expectedId,
            expectedName,
            expectedDescription,
            expectedActive,
            expectedType,
            expectedCreatedAt,
            expectedDeletedAt);

        final var actualJson = this.json.write(response);

        assertThat(actualJson)
            .hasJsonPathStringValue("$.id", expectedId)
            .hasJsonPathStringValue("$.name", expectedName)
            .hasJsonPathStringValue("$.description", expectedDescription)
            .hasJsonPathBooleanValue("$.is_active", expectedActive)
            .hasJsonPathStringValue("$.type", expectedType.name())
            .hasJsonPathStringValue("$.created_at", expectedCreatedAt.toString())
            .hasJsonPathStringValue("$.deleted_at", expectedDeletedAt.toString());
    }

}
