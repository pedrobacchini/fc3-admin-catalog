package com.github.pedrobacchini.admin.catalog.infrastructure.api.controller;

import com.github.pedrobacchini.admin.catalog.application.category.create.CreateCategoryCommand;
import com.github.pedrobacchini.admin.catalog.application.category.create.CreateCategoryOutput;
import com.github.pedrobacchini.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.delete.DeleteCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.github.pedrobacchini.admin.catalog.application.category.update.UpdateCategoryCommand;
import com.github.pedrobacchini.admin.catalog.application.category.update.UpdateCategoryOutput;
import com.github.pedrobacchini.admin.catalog.application.category.update.UpdateCategoryUseCase;
import com.github.pedrobacchini.admin.catalog.domain.pagination.Pagination;
import com.github.pedrobacchini.admin.catalog.domain.validation.handler.Notification;
import com.github.pedrobacchini.admin.catalog.infrastructure.api.CategoryAPI;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.CategoryApiOuput;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.CreateCategoryApiInput;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.model.UpdateCategoryApiInput;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;

    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(
        final CreateCategoryUseCase createCategoryUseCase,
        final GetCategoryByIdUseCase getCategoryByIdUseCase,
        final UpdateCategoryUseCase updateCategoryUseCase,
        final DeleteCategoryUseCase deleteCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInput input) {
        final var aCommand = CreateCategoryCommand.with(
            input.name(),
            input.description(),
            input.active() != null ? input.active() : true,
            input.type());

        final Function<Notification, ResponseEntity<?>> onError = notification ->
            ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
            ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return createCategoryUseCase.execute(aCommand)
            .fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(
        final String search,
        final int page,
        final int perPage,
        final String sort,
        final String direction) {
        return null;
    }

    @Override
    public CategoryApiOuput getById(final String id) {
        return CategoryApiPresenter.present(getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryApiInput input) {
        final var aCommand = UpdateCategoryCommand.with(
            id,
            input.name(),
            input.description(),
            input.active() != null ? input.active() : true);

        final Function<Notification, ResponseEntity<?>> onError = notification ->
            ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return updateCategoryUseCase.execute(aCommand)
            .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(final String anId) {
        deleteCategoryUseCase.execute(anId);
    }

}
