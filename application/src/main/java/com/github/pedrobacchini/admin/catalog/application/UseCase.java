package com.github.pedrobacchini.admin.catalog.application;

import com.github.pedrobacchini.admin.catalog.domain.category.Category;

public class UseCase {

    public Category execute() {
        return Category.newCategory("Filmes", "A categoria mais assistida", true);
    }
}