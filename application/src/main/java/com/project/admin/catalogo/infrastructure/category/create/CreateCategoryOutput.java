package com.project.admin.catalogo.infrastructure.category.create;

import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryID;

public record CreateCategoryOutput(String id) {
    public static CreateCategoryOutput from(final Category aCategory){
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }
    public static CreateCategoryOutput from(final CategoryID categoryID){
        return new CreateCategoryOutput(categoryID.getValue());
    }
}
