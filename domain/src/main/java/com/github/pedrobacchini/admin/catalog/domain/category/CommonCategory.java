package com.github.pedrobacchini.admin.catalog.domain.category;

import java.time.Instant;

public final class CommonCategory extends Category implements CategoryDeactivate, Cloneable {

    private CommonCategory(
        final CategoryID anId,
        final String aName,
        final String aDescription,
        final boolean isActive,
        final Instant aCreatedInstant,
        final Instant aUpdatedInstant,
        final Instant aDeletedInstant) {
        super(anId, aName, aDescription, isActive, aCreatedInstant, aUpdatedInstant, aDeletedInstant);
    }

    private CommonCategory(
        final String aName,
        final String aDescription,
        final boolean isActive) {
        super(aName, aDescription, isActive);
    }

    public static CommonCategory newCategory(
        final String aName,
        final String aDescription,
        final boolean isActive) {
        return new CommonCategory(aName, aDescription, isActive);
    }

    public static CommonCategory with(
        final CategoryID id,
        final String name,
        final String description,
        final boolean active,
        final Instant createdAt,
        final Instant updatedAt,
        final Instant deletedAt) {
        return new CommonCategory(id, name, description, active, createdAt, updatedAt, deletedAt);
    }

    @Override
    public CommonCategory update(final String aName, final String aDescription) {
        return (CommonCategory) super.update(aName, aDescription);
    }

    @Override
    public void setDeletedAt(final Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public void setActive(final boolean active) {
        this.active = active;
    }

    @Override
    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public CommonCategory clone() {
        try {
            return (CommonCategory) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}
