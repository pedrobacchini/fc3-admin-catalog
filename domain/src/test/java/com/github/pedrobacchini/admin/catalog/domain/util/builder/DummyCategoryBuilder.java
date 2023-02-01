package com.github.pedrobacchini.admin.catalog.domain.util.builder;

import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.github.pedrobacchini.admin.catalog.domain.util.DummyUtil.dummyObject;

@Setter
@Accessors(fluent = true)
public class DummyCategoryBuilder {

    private String name = dummyObject(String.class);
    private String description = dummyObject(String.class);
    private boolean active = dummyObject(boolean.class);
    private CategoryType type = dummyObject(CategoryType.class);

    public Category build() {
        return Category.newCategory(name, description, active, type);
    }
}
