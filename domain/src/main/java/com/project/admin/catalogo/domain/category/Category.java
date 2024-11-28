package com.project.admin.catalogo.domain.category;

import com.project.admin.catalogo.domain.AggregateRoot;
import com.project.admin.catalogo.domain.validation.ValidationHandler;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Objects;


@Getter
@Setter
public class Category extends AggregateRoot<CategoryID> implements  Cloneable  {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "createdAt should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdatedAt, "updatedAt should not be nul");
        this.deletedAt = aDeletedAt;
    }


    public static Category newCategory(final String name, final String description, final Boolean isActive){

        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;

         return new Category(id, name, description, isActive, now, now, deletedAt);
    }


    public static Category with(
            final CategoryID anId,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new Category(
                anId,
                name,
                description,
                active,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public static Category with(final Category aCategory) {
        return with(
                aCategory.getId(),
                aCategory.name,
                aCategory.description,
                aCategory.isActive(),
                aCategory.createdAt,
                aCategory.updatedAt,
                aCategory.deletedAt
        );
    }


    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(this,handler).validate();
    }


    public Category deactivate(){
        if(getDeletedAt() == null){
            this.deletedAt = Instant.now();
        }
        this.active = false;
        this.updatedAt = Instant.now();

        return this;
    }

    public Category activate(){
        this.deletedAt = null;

        this.active = true;
        this.updatedAt = Instant.now();

        return this;
    }

    public Category update(final String aName, final String aDescription, final Boolean isActive){
        if(isActive){
            activate();
        }else {
            deactivate();
        }

        this.name = aName;
        this.description = aDescription;
        this.updatedAt = Instant.now();

        return this;
    }

    @Override
    public Category clone() {
        try {
            Category clone = (Category) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }


}
