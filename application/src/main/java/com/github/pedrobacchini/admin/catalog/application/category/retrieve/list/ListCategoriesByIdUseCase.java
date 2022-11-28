package com.github.pedrobacchini.admin.catalog.application.category.retrieve.list;

import com.github.pedrobacchini.admin.catalog.application.UseCase;
import com.github.pedrobacchini.admin.catalog.domain.category.CategorySearchQuery;
import com.github.pedrobacchini.admin.catalog.domain.pagination.Pagination;

public abstract class ListCategoriesByIdUseCase extends UseCase<CategorySearchQuery, Pagination<CaregoryListOutput>> {

}
