package com.github.pedrobacchini.admin.catalog.infrastructure.category.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;

import java.time.Instant;

public record CategoryApiOuput(
    String id,
    String name,
    String description,
    @JsonProperty("is_active") Boolean active,
    CategoryType type,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt,
    @JsonProperty("deleted_at") Instant deletedAt
) {

}
