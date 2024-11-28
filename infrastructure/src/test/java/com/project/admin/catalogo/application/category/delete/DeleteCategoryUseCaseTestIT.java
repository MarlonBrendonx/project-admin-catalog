package com.project.admin.catalogo.application.category.delete;

import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryGateway;
import com.project.admin.catalogo.domain.category.CategoryID;
import com.project.admin.catalogo.infrastructure.IntegrationTest;
import com.project.admin.catalogo.infrastructure.category.delete.DeleteCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.project.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

@IntegrationTest
public class DeleteCategoryUseCaseTestIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private  CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsDeleteCategory_thenShouldBeOk(){
        Category aCategory = Category.newCategory("Filmes","", true);
        final var expectedId = aCategory.getId();

        save(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertDoesNotThrow(()->useCase.execute(expectedId));
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test()
    public void givenAInvalidId_whenCallsDeleteCategory_thenShouldBeOk(){
        final var expectedId = CategoryID.from("123");
        Assertions.assertEquals(0, categoryRepository.count());

        Assertions.assertDoesNotThrow(()->useCase.execute(expectedId));

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test()
    public void givenAValid_whenGatewayTrowsException_shouldReturnException(){
        final var expectedId = CategoryID.from("1234");

        Mockito.doThrow(new IllegalStateException("Gateway Error"))
                .when(categoryGateway).deletedById(Mockito.eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, ()->useCase.execute(expectedId));
        Mockito.verify(categoryGateway, Mockito.times(1)).deletedById(Mockito.eq(expectedId));
    }


    private void save(final Category... aCategory) {
        final var categoryList = Arrays.stream(aCategory)
                .map(CategoryJpaEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categoryList);
    }

}
