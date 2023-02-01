package com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence;

import com.github.pedrobacchini.admin.catalog.domain.category.Category;
import com.github.pedrobacchini.admin.catalog.domain.category.CategoryType;
import com.github.pedrobacchini.admin.catalog.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MySQLGatewayTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessageError = "not-null property references a null or transient value : com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.name";
        final var aCategory = Category.newCategory("Film", "A categoria", true, CategoryType.COMMON);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setName(null);

        final var actualException = assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(expectedPropertyName, actualCause.getPropertyName());
        assertEquals(expectedMessageError, actualCause.getMessage());
    }

//    @Test
//    void givenAnInvalidNullActive_whenCallsSave_shouldReturnError() {
//        final var expectedPropertyName = "active";
//        final var expectedMessageError = "not-null property references a null or transient value : com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.active";
//        final var aCategory = Category.newCategory("Film", "A categoria", true, CategoryType.COMMON);
//
//        final var anEntity = CategoryJpaEntity.from(aCategory);
//        anEntity.setActive(null);
//
//        final var actualException = assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));
//
//        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());
//
//        assertEquals(expectedPropertyName, actualCause.getPropertyName());
//        assertEquals(expectedMessageError, actualCause.getMessage());
//    }

    @Test
    void givenAnInvalidNullType_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "type";
        final var expectedMessageError = "not-null property references a null or transient value : com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.type";
        final var aCategory = Category.newCategory("Film", "A categoria", true, CategoryType.COMMON);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setType(null);

        final var actualException = assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(expectedPropertyName, actualCause.getPropertyName());
        assertEquals(expectedMessageError, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "createdAt";
        final var expectedMessageError = "not-null property references a null or transient value : com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.createdAt";
        final var aCategory = Category.newCategory("Film", "A categoria", true, CategoryType.COMMON);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setCreatedAt(null);

        final var actualException = assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(expectedPropertyName, actualCause.getPropertyName());
        assertEquals(expectedMessageError, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "updatedAt";
        final var expectedMessageError = "not-null property references a null or transient value : com.github.pedrobacchini.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";
        final var aCategory = Category.newCategory("Film", "A categoria", true, CategoryType.COMMON);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setUpdatedAt(null);

        final var actualException = assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(expectedPropertyName, actualCause.getPropertyName());
        assertEquals(expectedMessageError, actualCause.getMessage());
    }

}