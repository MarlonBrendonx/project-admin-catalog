package com.project.admin.catalogo.infrastructure.category.retrieve.list;

import com.project.admin.catalogo.infrastructure.UseCase;
import com.project.admin.catalogo.domain.category.CategorySearchQuery;
import com.project.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoryUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
