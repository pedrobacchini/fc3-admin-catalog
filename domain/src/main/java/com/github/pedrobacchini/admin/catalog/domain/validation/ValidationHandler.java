package com.github.pedrobacchini.admin.catalog.domain.validation;

import java.util.List;
import java.util.Optional;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

//    ValidationHandler append(ValidationHandler anHandler);

//    ValidationHandler validate(Validation aValidation);

    List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default Optional<Error> firstError() {
        return getErrors() != null && !getErrors().isEmpty() ? Optional.of(getErrors().get(0)) : Optional.empty();
    }

    interface Validation {

        void validate();

    }

}
