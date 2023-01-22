package com.github.pedrobacchini.admin.catalog.domain.category;

import java.time.Instant;

public interface CategoryDeactivate {

    Instant getDeletedAt();

    void setDeletedAt(Instant deletedAt);

    void setActive(boolean active);

    void setUpdatedAt(Instant updatedAt);

    default void deactivate() {
        final Instant now = Instant.now();
        if (getDeletedAt() == null) {
            setDeletedAt(now);
        }
        setActive(false);
        setUpdatedAt(now);
    }

    default void activate() {
        setDeletedAt(null);
        setActive(true);
        setUpdatedAt(Instant.now());
    }

}
