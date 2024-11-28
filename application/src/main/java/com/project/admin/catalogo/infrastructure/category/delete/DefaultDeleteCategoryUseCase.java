package com.project.admin.catalogo.infrastructure.category.delete;

import com.project.admin.catalogo.domain.category.CategoryGateway;
import com.project.admin.catalogo.domain.category.CategoryID;


import java.util.Objects;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public void execute(CategoryID id) {
       this.categoryGateway.deletedById(id);
    }

}
