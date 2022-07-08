package com.github.pedrobacchini.admin.catalog.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UseCaseTest {

    @Test
    void testCreateUseCase() {
        assertNotNull(new UseCase());
        assertNotNull(new UseCase().execute());
    }

}