package com.github.pedrobacchini.admin.catalog.application.category.retrieve.get;

import com.github.pedrobacchini.admin.catalog.domain.category.CategoryGateway;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;
import com.github.pedrobacchini.admin.catalog.domain.exception.DomainException;
import com.github.pedrobacchini.admin.catalog.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(final String anId) {
        final var anCategoryID = CategoryID.from(anId);
        return this.categoryGateway.findById(anCategoryID)
            .map(CategoryOutput::from)
            .orElseThrow(notFound(anCategoryID));
    }

    private Supplier<DomainException> notFound(final CategoryID anId) {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(anId.getValue())));
    }

}