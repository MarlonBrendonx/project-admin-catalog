package com.project.admin.catalogo.application.category.retrieve.get;

import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryGateway;
import com.project.admin.catalogo.domain.category.CategoryID;
import com.project.admin.catalogo.domain.exceptions.NotFoundException;
import com.project.admin.catalogo.infrastructure.IntegrationTest;
import com.project.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.project.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.project.admin.catalogo.infrastructure.category.retrieve.get.RetrieveCategoryByIdUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

@IntegrationTest
public class RetrieveCategoryByIdUseCaseTestIT {

    @Autowired
    private RetrieveCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;


    @Test
    public void givenACategoryId_whenCallFindById_thenReturnCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId();

        save(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualCategory = useCase.execute(aCategory.getId().getValue());

        Assertions.assertEquals(expectedId.getValue(), actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertEquals(aCategory.getCreatedAt().truncatedTo(ChronoUnit.MILLIS), actualCategory.createdAt().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertEquals(aCategory.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS), actualCategory.updatedAt().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertNull(actualCategory.deletedAt());
    }


    @Test
    public void givenAInvalidId_whenCallFindById_thenReturnNotFound() {
        final var expectedErrorMessage = "Category with ID 1234 was not found";
        final var expectedId = CategoryID.from("1234");


        final var actualException = Assertions.assertThrows(NotFoundException.class, ()->useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());
    }

    @Test
    public void givenAInvalidId_whenGatewayThrowsException_thenReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = CategoryID.from("1234");

        doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).findById(expectedId);

        final var actualException = Assertions.assertThrows(IllegalStateException.class, ()->useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());

    }

    private void save(final Category... aCategory) {
        final var categoryList = Arrays.stream(aCategory)
                        .map(CategoryJpaEntity::from)
                                .toList();

        categoryRepository.saveAllAndFlush(categoryList);
    }


}
