package com.github.pedrobacchini.admin.catalog.infrastructure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testMain() {
        assertNotNull(new Main());
        Main.main(new String[]{});
    }

}