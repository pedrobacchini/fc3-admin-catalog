package com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence;

import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {



}
