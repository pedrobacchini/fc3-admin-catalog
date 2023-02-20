package com.github.pedrobacchini.admin.catalog.infrastructure.category.presenters;

import com.github.pedrobacchini.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.CategoryApiOuput;

public interface CategoryApiPresenter {

    static CategoryApiOuput present(final CategoryOutput categoryOutput) {
        return new CategoryApiOuput(
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
}
