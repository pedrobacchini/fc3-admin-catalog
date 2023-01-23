package com.github.pedrobacchini.admin.catalog.domain.category;

import com.github.pedrobacchini.admin.catalog.domain.AggregateRoot;
import com.github.pedrobacchini.admin.catalog.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private CategoryType type;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
        final CategoryID anId,
        final String aName,
        final String aDescription,
        final boolean isActive,
        final CategoryType aType,
        final Instant aCreatedInstant,
        final Instant aUpdatedInstant,
        final Instant aDeletedInstant) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.type = aType;
        this.createdAt = Objects.requireNonNull(aCreatedInstant, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdatedInstant, "'updatedAt' should not be null");
        this.deletedAt = aDeletedInstant;
    }

    public static Category newCategory(
        final String aName,
        final String aDescription,
        final boolean isActive,
        final CategoryType aType) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Category(id, aName, aDescription, isActive, aType, now, now, deletedAt);
    }

    public static Category with(
        final CategoryID id,
        final String name,
        final String description,
        final boolean active,
        final CategoryType type,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt) {
        return new Category(id, name, description, active, type, createdAt, updatedAt, deletedAt);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category deactivate() {
        final Instant now = Instant.now();
        if (getDeletedAt() == null) {
            this.deletedAt = now;
        }
        this.active = false;
        this.updatedAt = now;
        return this;
    }

    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category update(
        final String aName,
        final String aDescription,
        final boolean isActive) {
        if (isActive)
            activate();
        else
            deactivate();
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

    public CategoryType getType() {
        return type;
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
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
