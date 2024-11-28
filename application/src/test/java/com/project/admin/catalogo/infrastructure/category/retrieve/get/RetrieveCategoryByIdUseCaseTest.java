package com.project.admin.catalogo.infrastructure.category.retrieve.get;


import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryGateway;
import com.project.admin.catalogo.domain.category.CategoryID;
import com.project.admin.catalogo.domain.exceptions.DomainException;
import com.project.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RetrieveCategoryByIdUseCaseTest  {

    @InjectMocks
    private DefaultRetrieveCategoryByIdUseCase useCase;

    @Mock
    CategoryGateway categoryGateway;


    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }
    // 1 - Buscar categoria por id
    // 2 - Buscar categoria com id inexistente
    // 3 - Erro no gateway ao buscar a categoria

    @Test
    public void givenACategoryId_whenCallFindById_thenReturnCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId();

        when(categoryGateway.findById(eq(expectedId))).thenReturn(
                Optional.of(aCategory.clone())
        );

        final var actualCategory = useCase.execute(aCategory.getId().getValue());

        Assertions.assertEquals(expectedId.getValue(), actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.createdAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.deletedAt());

    }

    @Test
    public void givenAInvalidId_whenCallFindById_thenReturnNotFound() {
        final var expectedErrorMessage = "Category with ID 1234 was not found";
        final var expectedId = CategoryID.from("1234");

        when(categoryGateway.findById(eq(expectedId))).thenReturn(
                Optional.empty()
        );
        final var actualException = Assertions.assertThrows(NotFoundException.class, ()->useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());
    }

    @Test
    public void givenAInvalidId_whenGatewayThrowsException_thenReturnException() {
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = CategoryID.from("1234");

        when(categoryGateway.findById(eq(expectedId))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(IllegalStateException.class, ()->useCase.execute(expectedId.getValue()));
        Assertions.assertEquals(expectedErrorMessage,actualException.getMessage());

    }
}
