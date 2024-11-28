package com.project.admin.catalogo.infrastructure.category.retrieve.list;

import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryGateway;
import com.project.admin.catalogo.domain.category.CategorySearchQuery;
import com.project.admin.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ListCategoryUseCaseTest {

    @InjectMocks
    private DefaultListCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;


    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

    // 1 - retornar lista de todas categorias
    // 2 - retornar lista com nenhuma categoria
    // 3 - retornar exception ao buscar lista com todas categorias


    @Test
    public void givenAValidQuery_whenCallFindAll_thenReturnCategoryList(){

        final var categories = List.of(
                Category.newCategory("Filmes", "",true),
                Category.newCategory("Series", "",true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 2;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;

        final var aQuery = new CategorySearchQuery(expectedPage,expectedPerPage,expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage,categories.size(), categories);

        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        Mockito.when(categoryGateway.findAll(aQuery)).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());

    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories(){
        final var categories = List.<Category>of();

        final var expectedPage = 0;
        final var expectedPerPage = 0;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;

        final var aQuery = new CategorySearchQuery(expectedPage,expectedPerPage,expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, 0, categories);

        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        Mockito.when(categoryGateway.findAll(aQuery)).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public  void givenAValidQuery_whenGatewayThrowsException_thenReturnException(){
        final var expectedPage = 0;
        final var expectedPerPage = 0;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedMessage = "Gateway Error";

        final var aQuery = new CategorySearchQuery(expectedPage,expectedPerPage,expectedTerms, expectedSort, expectedDirection);

        Mockito.when(categoryGateway.findAll(aQuery)).thenThrow(new IllegalStateException(expectedMessage));


        final var actualException = Assertions.assertThrows(IllegalStateException.class, ()-> useCase.execute(aQuery));

        Assertions.assertEquals(actualException.getMessage(), expectedMessage);

    }
}
