package com.project.admin.catalogo.infrastructure.category.presenters;

import com.project.admin.catalogo.infrastructure.category.models.CategoryAPIOutput;
import com.project.admin.catalogo.infrastructure.category.models.CategoryListAPIOutput;
import com.project.admin.catalogo.infrastructure.category.retrieve.get.RetrieveCategoryOutput;
import com.project.admin.catalogo.infrastructure.category.retrieve.list.CategoryListOutput;
import com.project.admin.catalogo.infrastructure.category.update.UpdateCategoryOutput;

public interface CategoryApiPresenter {

//    Function<RetrieveCategoryOutput, CategoryAPIOutput> present = output-> new CategoryAPIOutput(
//            output.id(),
//            output.name(),
//            output.description(),
//            output.active(),
//            output.createdAt(),
//            output.updatedAt(),
//            output.deletedAt()
//    );

    static CategoryAPIOutput present(RetrieveCategoryOutput retrieveCategoryOutput){
        return new CategoryAPIOutput(
          retrieveCategoryOutput.id(),
          retrieveCategoryOutput.name(),
          retrieveCategoryOutput.description(),
          retrieveCategoryOutput.active(),
                retrieveCategoryOutput.createdAt(),
                retrieveCategoryOutput.updatedAt(),
                retrieveCategoryOutput.deletedAt()
        );
    }

    static CategoryListAPIOutput present(CategoryListOutput categoryListOutput){
        return new CategoryListAPIOutput(
                categoryListOutput.id(),
                categoryListOutput.name(),
                categoryListOutput.description(),
                categoryListOutput.active(),
                categoryListOutput.createdAt(),
                categoryListOutput.updatedAt(),
                categoryListOutput.deletedAt()
        );
    }

}
