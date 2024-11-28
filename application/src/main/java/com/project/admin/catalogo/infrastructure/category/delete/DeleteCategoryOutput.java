package com.project.admin.catalogo.infrastructure.category.delete;

import com.project.admin.catalogo.domain.category.CategoryID;

public record DeleteCategoryOutput(CategoryID id) {

    public static DeleteCategoryOutput from (CategoryID id){
        return new DeleteCategoryOutput(id);
    }

}
