package com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence;

import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;
import com.github.pedrobacchini.admin.catalog.infrastructure.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @Disabled
    void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var aCategory = Category.newCategory("Film", "A categoria", true, CategoryType.COMMON);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setId(null);

        final var actualException = assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals("name", actualCause.getPropertyName());
        assertEquals("", actualCause.getMessage());
    }

}