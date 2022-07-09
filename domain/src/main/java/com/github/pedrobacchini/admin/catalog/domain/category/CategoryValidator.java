package com.github.pedrobacchini.admin.catalog.domain.category;

import com.github.pedrobacchini.admin.catalog.domain.validation.Error;
import com.github.pedrobacchini.admin.catalog.domain.validation.ValidationHandler;
import com.github.pedrobacchini.admin.catalog.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private Category category;

    public CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        category = aCategory;
    }

    @Override
    public void validate() {
        if(this.category.getName() == null) {
            this.validationHandler().append(new Error("'name' should not be bull"));
        }
    }

}
