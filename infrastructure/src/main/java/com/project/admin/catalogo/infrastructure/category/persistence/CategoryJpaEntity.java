package com.project.admin.catalogo.infrastructure.category.persistence;


import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "categories")
public class CategoryJpaEntity {

    @Id
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = true, length = 4000)
    private String description;
    @Column(name = "active", nullable = true)
    private Boolean active;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;
    @Column(name = "deleted_at",  columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public static CategoryJpaEntity from(Category category){
        return new CategoryJpaEntity(
                category.getId().getValue(),
                category.getName(),
                category.getDescription(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt(),
                category.getDeletedAt()
        );
    }

    public Category toAggregate(){
        return  Category.with(
                CategoryID.from(this.getId()),
                this.getName(),
                this.getDescription(),
                this.getActive(),
                this.getCreatedAt(),
                this.getUpdatedAt(),
                this.getDeletedAt()
        );
    }

}
