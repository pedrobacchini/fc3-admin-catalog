package com.github.pedrobacchini.admin.catalog.infrastructure.category;

import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryGateway;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryID;
import com.github.pedrobacchini.admin.catalog.domain.category.CategorySearchQuery;
import com.github.pedrobacchini.admin.catalog.domain.pagination.Pagination;
import com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryMySQLGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = requireNonNull(categoryRepository);
    }

    @Override
    public Category create(final Category aCategory) {
        return null;
    }

    @Override
    public void deleteById(final CategoryID anId) {

    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return Optional.empty();
    }

    @Override
    public Category update(final Category aCategory) {
        return null;
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        return null;
    }

}
