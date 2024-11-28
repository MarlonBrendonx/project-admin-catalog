package com.project.admin.catalogo.domain;

import com.project.admin.catalogo.domain.validation.ValidationHandler;
import lombok.Getter;

import java.util.Objects;


@Getter
public abstract class Entity<ID extends  Identifier> {

    protected final ID id;

    public Entity(final ID id) {
        Objects.requireNonNull(id, "id should not be null");

        this.id = id;
    }

    public abstract void validate(ValidationHandler handler);

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
