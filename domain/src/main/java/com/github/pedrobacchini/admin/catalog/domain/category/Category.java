package com.github.pedrobacchini.admin.catalog.domain.category;

import com.github.pedrobacchini.admin.catalog.domain.AggregateRoot;
import com.github.pedrobacchini.admin.catalog.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> {
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
        final CategoryID anId,
        final String aName,
        final String aDescription,
        final boolean isActive,
        final Instant aCreatedInstant,
        final Instant aUpdatedInstant,
        final Instant aDeletedInstant) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = aCreatedInstant;
        this.updatedAt = aUpdatedInstant;
        this.deletedAt = aDeletedInstant;
    }

    public static Category newCategory(
        final String aName,
        final String aDescription,
        final boolean isActive) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        return new Category(id, aName, aDescription, isActive, now, now, null);
    }

    public CategoryID getCategoryID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

}
