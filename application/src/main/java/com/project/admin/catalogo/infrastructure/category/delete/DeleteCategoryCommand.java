package com.project.admin.catalogo.infrastructure.category.delete;

import com.project.admin.catalogo.domain.category.CategoryID;

public record DeleteCategoryCommand(CategoryID id) {
}
