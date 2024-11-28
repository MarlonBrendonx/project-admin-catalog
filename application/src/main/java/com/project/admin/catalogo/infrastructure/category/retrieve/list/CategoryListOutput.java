package com.project.admin.catalogo.infrastructure.category.retrieve.list;

import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryID;

import java.time.Instant;

public record CategoryListOutput(
        String id,
        String name,
        String description,
        boolean active,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static CategoryListOutput from(Category aCategory){
        return new CategoryListOutput(
                aCategory.getId().getValue(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeletedAt()
        );
    }
}
