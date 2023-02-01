package com.github.pedrobacchini.admin.catalog.infrastructure.api;

import com.github.pedrobacchini.admin.catalog.ControllerTest;
import com.github.pedrobacchini.admin.catalog.application.category.create.DefaultCreateCategoryUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DefaultCreateCategoryUseCase createCategoryUseCase;

    @Test
    void name() {

    }

}
