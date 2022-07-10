package com.github.pedrobacchini.admin.catalog.application.category.update;

import com.github.pedrobacchini.admin.catalog.application.UseCase;
import com.github.pedrobacchini.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {


}
