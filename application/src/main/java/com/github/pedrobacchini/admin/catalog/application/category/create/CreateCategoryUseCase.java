package com.github.pedrobacchini.admin.catalog.application.category.create;

import com.github.pedrobacchini.admin.catalog.application.UseCase;
import com.github.pedrobacchini.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {


}
