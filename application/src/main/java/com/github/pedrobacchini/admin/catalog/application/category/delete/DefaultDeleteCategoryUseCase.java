package com.github.pedrobacchini.admin.catalog.application.category.delete;

import com.github.pedrobacchini.admin.catalog.domain.category.CategoryGateway;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;

import java.util.Objects;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public void execute(final String anId) {
        this.categoryGateway.deleteById(CategoryID.from(anId));
    }

}
