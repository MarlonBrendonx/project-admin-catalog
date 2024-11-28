package com.project.admin.catalogo.domain.category;

import com.project.admin.catalogo.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {
    Category create(Category aCategory);
    void deletedById(CategoryID anID);
    Optional<Category> findById(CategoryID anID);
    Category update(Category aCategory);
    Pagination<Category> findAll(CategorySearchQuery aQuery);
   // List<CategoryID> existsByIds(Iterable<CategoryID> ids);
}
