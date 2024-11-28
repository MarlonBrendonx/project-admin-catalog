package com.project.admin.catalogo.infrastructure.category.retrieve.get;

import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryGateway;
import com.project.admin.catalogo.domain.category.CategoryID;
import com.project.admin.catalogo.domain.exceptions.DomainException;
import com.project.admin.catalogo.domain.exceptions.NotFoundException;
import com.project.admin.catalogo.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultRetrieveCategoryByIdUseCase extends RetrieveCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultRetrieveCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public RetrieveCategoryOutput execute(String anId) {

        final var anCategoryID = CategoryID.from(anId);

        return this.categoryGateway.findById(anCategoryID)
                .map(RetrieveCategoryOutput::from)
                .orElseThrow(notFound(anCategoryID));
    }



    private Supplier<NotFoundException> notFound(final CategoryID anId) {
        return () -> NotFoundException.with(Category.class, anId);
    }
}
