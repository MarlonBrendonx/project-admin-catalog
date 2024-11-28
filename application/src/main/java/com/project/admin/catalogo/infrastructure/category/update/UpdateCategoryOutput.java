package com.project.admin.catalogo.infrastructure.category.update;

import com.project.admin.catalogo.domain.category.Category;


public record UpdateCategoryOutput(
    String id
) {

    public static UpdateCategoryOutput from(final Category aCategory){
        return new UpdateCategoryOutput(aCategory.getId().getValue());
    }

    public static UpdateCategoryOutput from(final String anID){
        return new UpdateCategoryOutput(anID);
    }
}
