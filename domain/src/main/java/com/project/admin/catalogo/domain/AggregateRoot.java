package com.project.admin.catalogo.domain;

import com.project.admin.catalogo.domain.validation.ValidationHandler;

public abstract class AggregateRoot<ID extends  Identifier> extends Entity<ID> {
    public AggregateRoot(final ID id) {
        super(id);
    }

}
