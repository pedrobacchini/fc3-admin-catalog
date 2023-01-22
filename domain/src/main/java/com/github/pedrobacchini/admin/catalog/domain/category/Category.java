package com.github.pedrobacchini.admin.catalog.domain.category;

import com.github.pedrobacchini.admin.catalog.domain.AggregateRoot;
import com.github.pedrobacchini.admin.catalog.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public abstract class Category extends AggregateRoot<CategoryID> {

    protected String name;
    protected String description;
    protected boolean active;
    protected Instant createdAt;
    protected Instant updatedAt;
    protected Instant deletedAt;

    protected Category(
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
        this.createdAt = Objects.requireNonNull(aCreatedInstant, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdatedInstant, "'updatedAt' should not be null");
        this.deletedAt = aDeletedInstant;
    }

    protected Category(
        final String aName,
        final String aDescription,
        final boolean isActive) {
        super(CategoryID.unique());
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        final var now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
        this.deletedAt = isActive ? null : now;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    protected Category update(
        final String aName,
        final String aDescription) {
        this.name = aName;
        this.description = aDescription;
        this.updatedAt = Instant.now();
        return this;
    }

    public CategoryID getId() {
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

}
