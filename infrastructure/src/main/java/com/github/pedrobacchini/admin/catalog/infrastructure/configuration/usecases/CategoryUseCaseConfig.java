package com.github.pedrobacchini.admin.catalog.infrastructure.configuration.usecases;

import com.github.pedrobacchini.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.create.DefaultCreateCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.delete.DefaultDeleteCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.list.ListCategoriesUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.update.DefaultUpdateCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

}
