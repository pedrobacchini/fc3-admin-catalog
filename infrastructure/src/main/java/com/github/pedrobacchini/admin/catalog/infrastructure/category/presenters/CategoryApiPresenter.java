package com.github.pedrobacchini.admin.catalog.infrastructure.category.presenters;

import com.github.pedrobacchini.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.list.CaregoryListOutput;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.CategoryResponse;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.CategoryListResponse;

public interface CategoryApiPresenter {

    static CategoryResponse present(final CategoryOutput categoryOutput) {
        return new CategoryResponse(
            categoryOutput.id().getValue(),
            categoryOutput.name(),
            categoryOutput.description(),
            categoryOutput.isActive(),
            categoryOutput.type(),
            categoryOutput.createdAt(),
            categoryOutput.updatedAt(),
            categoryOutput.deletedAt()
        );
    }

    static CategoryListResponse present(final CaregoryListOutput categoryOutput) {
        return new CategoryListResponse(
            categoryOutput.id(),
            categoryOutput.name(),
            categoryOutput.description(),
            categoryOutput.isActive(),
            categoryOutput.type(),
            categoryOutput.createdAt(),
            categoryOutput.deletedAt()
        );
    }
}
