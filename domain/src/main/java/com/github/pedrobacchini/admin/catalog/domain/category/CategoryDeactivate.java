package com.github.pedrobacchini.admin.catalog.domain.category;

import java.time.Instant;

public interface CategoryDeactivate {

    Instant getDeletedAt();

    void setDeletedAt(Instant deletedAt);

    void setActive(boolean active);

    void setUpdatedAt(Instant updatedAt);

    default <T extends Category> T deactivate(final T category) {
        final Instant now = Instant.now();

        if (category instanceof CategoryDeactivate cd) {
            if (cd.getDeletedAt() == null) {
                cd.setDeletedAt(now);
            }
            cd.setActive(false);
            cd.setUpdatedAt(now);
        }

        return category;
    }

    default <T extends Category> T activate(final T category) {
        if (category instanceof CategoryDeactivate cd) {
            cd.setDeletedAt(null);
            cd.setActive(true);
            cd.setUpdatedAt(Instant.now());
        }

        return category;
    }

}
