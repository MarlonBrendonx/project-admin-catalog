package com.project.admin.catalogo.infrastructure.category.retrieve.get;
import com.project.admin.catalogo.domain.category.Category;

import java.time.Instant;

public record RetrieveCategoryOutput(
         String id,
         String name,
         String description,
         boolean active,
         Instant createdAt,
         Instant updatedAt,
         Instant deletedAt
) {

    public static RetrieveCategoryOutput from(final  Category aCategory){
        return new RetrieveCategoryOutput(
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
