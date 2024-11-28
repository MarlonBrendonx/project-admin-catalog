package com.project.admin.catalogo.infrastructure.category.create;

import com.project.admin.catalogo.infrastructure.UseCase;
import com.project.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
