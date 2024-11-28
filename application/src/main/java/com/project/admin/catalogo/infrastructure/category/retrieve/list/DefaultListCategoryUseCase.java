package com.project.admin.catalogo.infrastructure.category.retrieve.list;

import com.project.admin.catalogo.domain.category.CategoryGateway;
import com.project.admin.catalogo.domain.category.CategorySearchQuery;
import com.project.admin.catalogo.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoryUseCase extends  ListCategoryUseCase{

     private final CategoryGateway categoryGateway;

    public DefaultListCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }


    @Override
    public Pagination<CategoryListOutput> execute(CategorySearchQuery aQuery) {

        return this.categoryGateway.findAll(aQuery)
                .map(CategoryListOutput::from);

    }
}
