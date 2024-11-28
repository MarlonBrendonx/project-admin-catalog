package com.project.admin.catalogo.application.category.update;

import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryGateway;
import com.project.admin.catalogo.domain.exceptions.NotFoundException;
import com.project.admin.catalogo.infrastructure.IntegrationTest;
import com.project.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.project.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.project.admin.catalogo.infrastructure.category.update.UpdateCategoryCommand;
import com.project.admin.catalogo.infrastructure.category.update.UpdateCategoryUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCategoryUseCaseTestIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;


    @Test
    public void givenAValidCommand_whenCallUpdateCategory_shouldReturnCategoryId() {

        final var aCategory =
                Category.newCategory("Film", null, true);

        save(aCategory);


        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();


        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.getActive());
        Assertions.assertEquals(aCategory.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS), actualCategory.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    // 2. Teste passando uma propriedade inválida (name)
    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory =
                Category.newCategory("Film", null, true);


        save(aCategory);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand =
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);


        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_thenShouldReturnInactiveCategoryId() {
        final var aCategory =
                Category.newCategory("Film", null, true);

        save(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        final String expectedName = "Filme";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand =
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);



        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.getActive());
        Assertions.assertEquals(aCategory.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS), actualCategory.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    // 4. Teste simulando um erro generico vindo do gateway
    @Test
    public void givenAInvalidCommand_whenCallsUpdateCategory_thenShouldReturnException() {
        final var aCategory =
                Category.newCategory("Film", null, true);

        save(aCategory);
        Assertions.assertEquals(1, categoryRepository.count());

        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = 1;

        final var aCommand =
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).update(any());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        final var actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive(), actualCategory.getActive());
        Assertions.assertEquals(aCategory.getCreatedAt().truncatedTo(ChronoUnit.MILLIS), actualCategory.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertEquals(aCategory.getUpdatedAt().truncatedTo(ChronoUnit.MILLIS), actualCategory.getCreatedAt().truncatedTo(ChronoUnit.MILLIS));
        Assertions.assertNull(actualCategory.getDeletedAt());

    }


    @Test
    public void givenAInvalidID_whenCallsUpdateCategory_thenShouldReturnNotFoundException() {
        final var aCategory =
                Category.newCategory("Film", null, true);

        // to update
        final String expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "Category with ID %s was not found".formatted(aCategory.getId().getValue());


        final var aCommand =
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(NotFoundException.class, ()->useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }



    private void save(final Category... aCategory) {
        final var categoryList = Arrays.stream(aCategory)
                .map(CategoryJpaEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categoryList);
    }

}
