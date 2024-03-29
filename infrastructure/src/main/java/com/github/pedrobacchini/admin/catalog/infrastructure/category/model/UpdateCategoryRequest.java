package com.github.pedrobacchini.admin.catalog.infrastructure.category.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateCategoryRequest(
    String name,
    String description,
    @JsonProperty("is_active") Boolean active
) {

}
