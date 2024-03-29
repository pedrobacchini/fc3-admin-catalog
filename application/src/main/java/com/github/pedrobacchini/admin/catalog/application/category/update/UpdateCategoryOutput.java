package com.github.pedrobacchini.admin.catalog.application.category.update;

import com.github.pedrobacchini.admin.catalog.domain.category.Category;

public record UpdateCategoryOutput(
    String id
) {

    public static UpdateCategoryOutput from(final String anId) {
        return new UpdateCategoryOutput(anId);
    }
    public static UpdateCategoryOutput from(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId().toString());
    }

}
