package com.github.pedrobacchini.admin.catalog.infrastructure;


import com.github.pedrobacchini.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(WebServerConfig.class, args);
    }


    @Bean
    @DependsOnDatabaseInitialization
    ApplicationRunner runner(
        CreateCategoryUseCase createCategoryUseCase,
        UpdateCategoryUseCase updateCategoryUseCase,
        DeleteCategoryUseCase deleteCategoryUseCase,
        GetCategoryByIdUseCase getCategoryByIdUseCase,
        ListCategoriesUseCase listCategoriesUseCase) {
        return args -> {

        };
    }
}
