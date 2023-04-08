package com.github.pedrobacchini.admin.catalog.e2e.category;

import com.github.pedrobacchini.admin.catalog.E2ETest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

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
    void works() {
        assertTrue(MY_SQL_CONTAINER.isRunning());
    }

}

