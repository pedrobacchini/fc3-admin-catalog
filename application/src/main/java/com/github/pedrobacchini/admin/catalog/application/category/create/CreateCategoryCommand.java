package com.github.pedrobacchini.admin.catalog.application.category.create;

import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;

public record CreateCategoryCommand(
    String name,
    String description,
    boolean isActive,
    CategoryType type
) {

    public static CreateCategoryCommand with(
        final String aName,
        final String aDescription,
        final boolean isActive,
        final CategoryType type) {
        return new CreateCategoryCommand(aName, aDescription, isActive, type);
    }

}
