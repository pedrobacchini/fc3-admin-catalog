package com.github.pedrobacchini.admin.catalog.application;

import com.github.pedrobacchini.admin.catalog.IntegrationTest;
import com.github.pedrobacchini.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@IntegrationTest
public class SampleIT {

    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void test() {
        assertNotNull(createCategoryUseCase);
        assertNotNull(categoryRepository);
    }

}
