package com.project.admin.catalogo.domain.category;

import com.project.admin.catalogo.domain.exceptions.DomainException;
import com.project.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CategoryTest {


    //give / when / then
    @Test
    @DisplayName("Given a valid params when instantiate a category")
    public void givenAValidParams_whenCallNewCategory_thenInstantiateACategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var category =  Category.newCategory(expectedName, expectedDescription, expectedIsActive);


        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription,category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());

    }

    @Test
    @DisplayName("Given a valid params when instantiate a category and name is null")
    public void givenAInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveErros(){
        final String expectedName = null;
        final String expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var category =  Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var categoryException = Assertions.assertThrows(DomainException.class, ()-> category.validate(
                new ThrowsValidationHandler()
        ));

        Assertions.assertEquals(expectedErrorCount,categoryException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,categoryException.getErrors().get(0).message());

    }

    @Test
    @DisplayName("Given a valid params when instantiate a category and name is empty")
    public void givenAInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveErrors(){
        final String expectedName = " ";
        final String expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var category =  Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var categoryException = Assertions.assertThrows(DomainException.class, ()-> category.validate(
                new ThrowsValidationHandler()
        ));

        Assertions.assertEquals(expectedErrorCount,categoryException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,categoryException.getErrors().get(0).message());

    }

    @Test
    @DisplayName("Given a invalid name when instantiate a category and length less then 3")
    public void givenAInvalidNameLengthLessThen3_whenCallNewCategoryAndValidate_thenShouldReceiveErrors(){
        final String expectedName = "ab ";
        final String expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var category =  Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var categoryException = Assertions.assertThrows(DomainException.class, ()-> category.validate(
                new ThrowsValidationHandler()
        ));

        Assertions.assertEquals(expectedErrorCount,categoryException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,categoryException.getErrors().get(0).message());

    }

    @Test
    @DisplayName("Given a invalid name when instantiate a category and length greater then 255")
    public void givenAInvalidNameLengthGreaterThen255_whenCallNewCategoryAndValidate_thenShouldReceiveErrors(){
        final String expectedName =
                """
                 is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's 
                 standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled
                 it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic 
                 typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset
                 sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus 
                 PageMaker including versions of Lorem Ipsum
                """ ;
        final String expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var category =  Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var categoryException = Assertions.assertThrows(DomainException.class, ()-> category.validate(
                new ThrowsValidationHandler()
        ));

        Assertions.assertEquals(expectedErrorCount,categoryException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage,categoryException.getErrors().get(0).message());

    }

    @Test
    @DisplayName("Given a valid description when instantiate a category and description is empty")
    public void givenAValidEmptyDescription_whenCallNewCategoryAndValidate_thenShouldReceiveErrors(){
        final var expectedName = "Filmes";
        final var expectedDescription = " ";
        final var expectedIsActive = true;


        final var category =  Category.newCategory(expectedName, expectedDescription, expectedIsActive);


        Assertions.assertDoesNotThrow(()->category.validate(
                new ThrowsValidationHandler()
        ));
        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription,category.getDescription());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNull(category.getDeletedAt());

    }

    @Test
    @DisplayName("Given a false isActive when instantiate a category")
    public void givenAValidFalseIsActive_whenCallNewCategoryAndValidate_thenShouldReceiveErrors(){
        final var expectedName = "Filmes";
        final var expectedDescription = " ";
        final var expectedIsActive = false;


        final var category =  Category.newCategory(expectedName, expectedDescription, expectedIsActive);


        Assertions.assertDoesNotThrow(()->category.validate(
                new ThrowsValidationHandler()
        ));
        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(expectedName, category.getName());
        Assertions.assertEquals(expectedDescription,category.getDescription());
        Assertions.assertEquals(expectedIsActive, category.isActive());
        Assertions.assertNotNull(category.getCreatedAt());
        Assertions.assertNotNull(category.getUpdatedAt());
        Assertions.assertNotNull(category.getDeletedAt());

    }

    @Test
    @DisplayName("Given a valid active category when call deactivate then return category inactive")
    public void givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactivated(){
        final var expectedName = "Filmes";
        final var expectedDescription = " ";
        final var expectedIsActive = false;


        final var category =  Category.newCategory(expectedName, expectedDescription, true);


        Assertions.assertDoesNotThrow(()->category.validate(
                new ThrowsValidationHandler()
        ));

         final var updatedAt = category.getUpdatedAt();

         Assertions.assertNull(category.getDeletedAt());
         Assertions.assertTrue(category.isActive());


         final var actualCategory = category.deactivate();

         Assertions.assertDoesNotThrow(()->category.validate(
                new ThrowsValidationHandler()
         ));

         Assertions.assertNotNull(actualCategory);
         Assertions.assertNotNull(actualCategory.getId());
         Assertions.assertEquals(expectedName, actualCategory.getName());
         Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
         Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
         Assertions.assertNotNull(actualCategory.getCreatedAt());
         Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
         Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    @DisplayName("Given a valid inactive category when call active then return category active")
    public void givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryActive(){
        final var expectedName = "Filmes";
        final var expectedDescription = " ";
        final var expectedIsActive = true;


        final var category =  Category.newCategory(expectedName, expectedDescription, false);


        Assertions.assertDoesNotThrow(()->category.validate(
                new ThrowsValidationHandler()
        ));

         final var updatedAt = category.getUpdatedAt();
         final var createdAt = category.getCreatedAt();

         Assertions.assertNotNull(category.getDeletedAt());
         Assertions.assertFalse(category.isActive());


         final var actualCategory = category.activate();

         Assertions.assertDoesNotThrow(()->category.validate(
                new ThrowsValidationHandler()
         ));

         Assertions.assertNotNull(actualCategory);
         Assertions.assertNotNull(actualCategory.getId());
         Assertions.assertEquals(expectedName, actualCategory.getName());
         Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
         Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
         Assertions.assertNotNull(actualCategory.getCreatedAt());
         Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
         Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
         Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    @DisplayName("Given a valid category when call update then return category updated")
    public void givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated(){
        final var expectedName = "Filmes";
        final var expectedDescription = " ";
        final var expectedIsActive = true;

        final var category =  Category.newCategory("Filme", "A categoria", expectedIsActive);

        Assertions.assertDoesNotThrow(()->category.validate(
                new ThrowsValidationHandler()
        ));

        final var updatedAt = category.getUpdatedAt();
        final var createdAt = category.getCreatedAt();

        final var actualCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(()->category.validate(
                new ThrowsValidationHandler()
        ));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    @DisplayName("Given a valid category when call update to inactive then return category updated")
    public void givenAValidCategory_whenCallUpdateToInactive_thenReturnCategoryUpdated(){
        final var expectedName = "Filmes";
        final var expectedDescription = " ";
        final var expectedIsActive = false;

        final var category =  Category.newCategory("Filme", "A categoria", true);

        Assertions.assertDoesNotThrow(()->category.validate(
                new ThrowsValidationHandler()
        ));

        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.getDeletedAt());

        final var updatedAt = category.getUpdatedAt();
        final var createdAt = category.getCreatedAt();

        final var actualCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(()->category.validate(
                new ThrowsValidationHandler()
        ));

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertEquals(createdAt, actualCategory.getCreatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

//    @Test
//    @DisplayName("Given a invalid category when call update with invalid params then return category updated")
//    public void givenAInvalidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated(){
//        final String expectedName = null;
//        final var expectedDescription = "A categoria mais assistida";
//        final var expectedIsActive = true;
//
//        final var expectedErrorCount = 1;
//        final var expectedErrorMessage = "'name' should not be null";
//
//
//        final var category =  Category.newCategory("Filme", "A categoria", true);
//
//        Assertions.assertDoesNotThrow(()->category.validate(
//                new ThrowsValidationHandler()
//        ));
//
//
//        Assertions.assertTrue(category.isActive());
//        Assertions.assertNull(category.getDeletedAt());
//
//        final var categoryException = Assertions.assertThrows(
//                DomainException.class,
//                ()-> category.update(expectedName, expectedDescription, expectedIsActive));
//
//
//        Assertions.assertEquals(expectedErrorCount,categoryException.getErrors().size());
//        Assertions.assertEquals(expectedErrorMessage,categoryException.getErrors().get(0).message());
//
//    }

}
