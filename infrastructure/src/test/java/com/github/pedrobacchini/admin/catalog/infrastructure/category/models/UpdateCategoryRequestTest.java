package com.github.pedrobacchini.admin.catalog.infrastructure.category.models;

import com.github.pedrobacchini.admin.catalog.JacksonTest;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.UpdateCategoryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JacksonTest
public class UpdateCategoryRequestTest {

    @Autowired
    private JacksonTester<UpdateCategoryRequest> json;

    @Test
    void testUnmarshall() throws IOException {
        final var expectedName = "Filmes";
        final var expectedDescription = "Filmes de ação";
        final var expectedActive = true;

      final var json = """
            {
              "name": "%s",
              "description": "%s",
              "is_active": %s
            }
            """.formatted(expectedName, expectedDescription, expectedActive);

        final var actualObject = this.json.parse(json);

        assertThat(actualObject)
            .hasFieldOrPropertyWithValue("name", expectedName)
            .hasFieldOrPropertyWithValue("description", expectedDescription)
            .hasFieldOrPropertyWithValue("active", expectedActive);
    }

}
