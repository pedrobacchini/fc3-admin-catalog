package com.github.pedrobacchini.admin.catalog.application.category.update;

import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryGateway;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;
import com.github.pedrobacchini.admin.catalog.domain.category.CommonCategory;
import com.github.pedrobacchini.admin.catalog.domain.category.RestrictCategory;
import com.github.pedrobacchini.admin.catalog.domain.exception.DomainException;
import com.github.pedrobacchini.admin.catalog.domain.validation.Error;
import com.github.pedrobacchini.admin.catalog.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand aCommand) {
        final var anId = CategoryID.from(aCommand.id());
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();

        final var aCategory = categoryGateway.findById(anId)
            .orElseThrow(notFound(anId));
        final var notification = Notification.create();
        getUpdateCategoryOutputs(aName, aDescription, isActive, aCategory, notification)
            .map(category -> {
                category.validate(notification);
                return category;
            });
        return notification.hasError() ? API.Left(notification) : update(aCategory);
    }

    private static Either<Notification, Category> getUpdateCategoryOutputs(
        final String aName,
        final String aDescription,
        final boolean isActive,
        final Category aCategory,
        final Notification notification) {
        if (aCategory instanceof final CommonCategory commonCategory) {
            final var aCategoryUpdated = commonCategory.update(aName, aDescription, isActive);
            return API.Right(aCategoryUpdated);
        } else if (aCategory instanceof final RestrictCategory restrictCategory) {
            if (isActive) {
                final var aCategoryUpdated = restrictCategory.update(aName, aDescription);
                return API.Right(aCategoryUpdated);
            } else {
                notification.append(new Error("cannot inactivate this category"));
            }
        } else {
            notification.append(new Error("type not supported"));
        }
        return API.Left(notification);
    }

    private Supplier<DomainException> notFound(final CategoryID anId) {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(anId.getValue())));
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        return API.Try(() -> this.categoryGateway.update(aCategory))
            .toEither()
            .bimap(Notification::create, UpdateCategoryOutput::from);
    }

}
