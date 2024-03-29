package com.github.pedrobacchini.admin.catalog.domain.category;

import com.github.pedrobacchini.admin.catalog.domain.validation.Error;
import com.github.pedrobacchini.admin.catalog.domain.validation.ValidationHandler;
import com.github.pedrobacchini.admin.catalog.domain.validation.Validator;

public class CategoryValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;
    private final Category category;

    public CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        category = aCategory;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkActiveConstraints();
        checkTypeConstraints();
    }

    private void checkNameConstraints() {
        final String name = this.category.getName();
        if(name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }
        if(name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }
        final int length = name.trim().length();
        if(length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }

    private void checkActiveConstraints() {
        if(!this.category.isActive() && this.category.getType() != null && this.category.getType().equals(CategoryType.RESTRICT))
            this.validationHandler().append(new Error("'active' cannot be false for restrict category"));
    }

    private void checkTypeConstraints() {
        if(this.category.getType() == null) this.validationHandler().append(new Error("'type' should not be null"));
    }

}
