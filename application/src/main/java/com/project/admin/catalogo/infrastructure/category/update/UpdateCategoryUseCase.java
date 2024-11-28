package com.project.admin.catalogo.infrastructure.category.update;

import com.project.admin.catalogo.infrastructure.UseCase;
import com.project.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
