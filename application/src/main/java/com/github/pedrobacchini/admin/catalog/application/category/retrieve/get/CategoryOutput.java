package com.github.pedrobacchini.admin.catalog.application.category.retrieve.get;

import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;

import java.time.Instant;

public record CategoryOutput(
    CategoryID id,
    String name,
    String description,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
) {

    public static CategoryOutput from(final Category category) {
        return new CategoryOutput(
            category.getID(),
            category.getName(),
            category.getDescription(),
            category.isActive(),
            category.getCreatedAt(),
            category.getUpdatedAt(),
            category.getDeletedAt()
        );
    }
}
