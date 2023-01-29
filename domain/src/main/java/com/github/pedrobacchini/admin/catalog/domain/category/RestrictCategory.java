package com.github.pedrobacchini.admin.catalog.domain.category;

import java.time.Instant;

public class RestrictCategory extends Category {

    private RestrictCategory(
        final CategoryID anId,
        final String aName,
        final String aDescription,
        final boolean isActive,
        final Instant aCreatedInstant,
        final Instant aUpdatedInstant,
        final Instant aDeletedInstant) {
        super(anId, aName, aDescription, isActive, aCreatedInstant, aUpdatedInstant, aDeletedInstant);
    }

    private RestrictCategory(
        final String aName,
        final String aDescription,
        final boolean isActive) {
        super(aName, aDescription, isActive);
    }

    public static RestrictCategory with(
        final CategoryID id,
        final String name,
        final String description,
        final boolean active,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt) {
        return new RestrictCategory(id, name, description, active, createdAt, updatedAt, deletedAt);
    }

    @Override
    public RestrictCategory update(final String aName, final String aDescription) {
        return (RestrictCategory) super.update(aName, aDescription);
    }

}
