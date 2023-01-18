package com.github.pedrobacchini.admin.catalog.infrastructure.category;

import com.github.pedrobacchini.admin.catalog.infrastructure.MySQLGatewayTest;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@MySQLGatewayTest
class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testInjectedDependencies() {
        assertNotNull(categoryMySQLGateway);
        assertNotNull(categoryRepository);
    }

}