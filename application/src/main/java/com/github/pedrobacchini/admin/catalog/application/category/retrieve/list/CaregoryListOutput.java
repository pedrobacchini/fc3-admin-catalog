package com.github.pedrobacchini.admin.catalog.application.category.retrieve.list;

import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;

import java.time.Instant;

public record CaregoryListOutput(
    CategoryID id,
    String name,
    String description,
    boolean isActive,
    CategoryType type,
    Instant createdAt,
    Instant deletedAt
) {

    public static CaregoryListOutput from(final Category category) {
        return new CaregoryListOutput(
            category.getId(),
            category.getName(),
            category.getDescription(),
            category.isActive(),
            category.getType(),
            category.getCreatedAt(),
            category.getDeletedAt()
        );
    }

}
