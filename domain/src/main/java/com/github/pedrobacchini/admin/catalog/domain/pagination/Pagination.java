package com.github.pedrobacchini.admin.catalog.domain.pagination;

import java.util.List;

public record Pagination<T>(
    int currentPage,
    int perPage,
    long total,
    List<T> items
) {

}
