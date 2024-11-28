package com.project.admin.catalogo.infrastructure.category.update;


import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryGateway;
import com.project.admin.catalogo.domain.category.CategoryID;
import com.project.admin.catalogo.domain.exceptions.DomainException;
import com.project.admin.catalogo.domain.exceptions.NotFoundException;
import com.project.admin.catalogo.domain.validation.Error;
import com.project.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;



import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUpdateCategoryUseCase  extends  UpdateCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand anCommand) {
        final var anId = CategoryID.from(anCommand.id());

        final var notification = Notification.create();


        final var aCategory = this.categoryGateway.findById(CategoryID.from(anCommand.id()))
                .orElseThrow(notFound(anId));

        aCategory.update(anCommand.name(), anCommand.description(), anCommand.isActive());
        aCategory.validate(notification);

        return notification.hasError() ? API.Left(notification): update(aCategory);

    }

    private Supplier<NotFoundException> notFound(final CategoryID anId) {
        return ()->NotFoundException.with(Category.class, CategoryID.from(anId.getValue()));
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {

        return API.Try(()->this.categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);

    }
}
