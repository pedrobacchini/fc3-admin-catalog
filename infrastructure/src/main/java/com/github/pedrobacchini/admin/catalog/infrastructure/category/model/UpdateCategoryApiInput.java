package com.github.pedrobacchini.admin.catalog.infrastructure.category.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateCategoryApiInput(
    String name,
    String description,
    @JsonProperty("is_active") Boolean active
) {

}
