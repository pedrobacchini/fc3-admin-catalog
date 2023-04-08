package com.github.pedrobacchini.admin.catalog.infrastructure.category.models;

import com.github.pedrobacchini.admin.catalog.JacksonTest;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@JacksonTest
public class CategoryResponseTest {

    @Autowired
    private JacksonTester<CategoryResponse> json;

    @Test
    void testMarshall() throws IOException {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "Filmes de todos os gêneros";
        final var expectedIsActive = false;
        final var expectedType = CategoryType.COMMON;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new CategoryResponse(
            expectedId,
            expectedName,
            expectedDescription,
            expectedIsActive,
            expectedType,
            expectedCreatedAt,
            expectedUpdatedAt,
            expectedDeletedAt
        );

        final var actualJson = this.json.write(response);

        assertThat(actualJson)
            .hasJsonPathValue("$.id", expectedId)
            .hasJsonPathValue("$.description", expectedDescription)
            .hasJsonPathValue("$.is_active", expectedIsActive)
            .hasJsonPathValue("$.type", expectedType)
            .hasJsonPathValue("$.created_at", expectedCreatedAt)
            .hasJsonPathValue("$.updated_at", expectedUpdatedAt)
            .hasJsonPathValue("$.deleted_at", expectedDeletedAt);
    }

}
