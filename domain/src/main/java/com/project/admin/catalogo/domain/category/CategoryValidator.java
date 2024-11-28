package com.project.admin.catalogo.domain.category;

import com.project.admin.catalogo.domain.validation.Error;
import com.project.admin.catalogo.domain.validation.ValidationHandler;
import com.project.admin.catalogo.domain.validation.Validator;
import lombok.Getter;

public class CategoryValidator extends Validator {

    private final Category category;
    private static final int NAME_MAX_LENGTH = 255 ;
    private static final int NAME_MIN_LENGTH = 3;

    public CategoryValidator(final Category category, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.category.getName();

        if(name == null){
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if(name.isBlank()){
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final var length = name.trim().length();

        if(length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH){
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
