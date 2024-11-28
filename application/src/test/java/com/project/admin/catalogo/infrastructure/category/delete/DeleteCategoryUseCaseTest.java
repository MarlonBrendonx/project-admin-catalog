package com.project.admin.catalogo.infrastructure.category.delete;

import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryGateway;
import com.project.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;



    // 1. Teste do caminho feliz
    // 2. Teste removendo com um id inexistente
    // 3. Teste gerando um erro generico do gateway



    @Test
    public void givenAValidId_whenCallsDeleteCategory_thenShouldBeOk(){
        Category aCategory = Category.newCategory("Filmes","", Boolean.valueOf(true));
        final var expectedId = aCategory.getId();

        Mockito.doNothing()
                .when(categoryGateway).deletedById(Mockito.eq(expectedId));
        Assertions.assertDoesNotThrow(()->useCase.execute(expectedId));
        Mockito.verify(categoryGateway, Mockito.times(1)).deletedById(Mockito.eq(expectedId));
    }

    @Test()
    public void givenAInvalidId_whenCallsDeleteCategory_thenShouldBeOk(){
        final var expectedId = CategoryID.from("123");

        Mockito.doNothing()
                .when(categoryGateway).deletedById(Mockito.eq(expectedId));

        Assertions.assertDoesNotThrow(()->useCase.execute(expectedId));
        Mockito.verify(categoryGateway, Mockito.times(1)).deletedById(Mockito.eq(expectedId));
    }

    @Test()
    public void givenAValid_whenGatewayTrowsException_shouldReturnException(){
        final var expectedId = CategoryID.from("1234");

        Mockito.doThrow(new IllegalStateException("Gateway Error"))
                .when(categoryGateway).deletedById(Mockito.eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, ()->useCase.execute(expectedId));
        Mockito.verify(categoryGateway, Mockito.times(1)).deletedById(Mockito.eq(expectedId));
    }

}
