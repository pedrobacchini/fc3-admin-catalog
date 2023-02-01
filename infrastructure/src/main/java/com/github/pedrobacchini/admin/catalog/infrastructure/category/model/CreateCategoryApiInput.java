package com.github.pedrobacchini.admin.catalog.infrastructure.category.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;

public record CreateCategoryApiInput(
    String name,
    String description,
    @JsonProperty("is_active") Boolean active,
    CategoryType type
) {

}
