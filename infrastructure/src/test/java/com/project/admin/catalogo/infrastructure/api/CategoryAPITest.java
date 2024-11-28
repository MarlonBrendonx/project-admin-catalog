package com.project.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryID;
import com.project.admin.catalogo.domain.category.CategorySearchQuery;
import com.project.admin.catalogo.domain.exceptions.DomainException;
import com.project.admin.catalogo.domain.exceptions.NotFoundException;
import com.project.admin.catalogo.domain.pagination.Pagination;
import com.project.admin.catalogo.domain.validation.Error;
import com.project.admin.catalogo.domain.validation.handler.Notification;
import com.project.admin.catalogo.infrastructure.ControllerTest;
import com.project.admin.catalogo.infrastructure.category.create.CreateCategoryOutput;
import com.project.admin.catalogo.infrastructure.category.create.CreateCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.delete.DeleteCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.models.CategoryAPIOutput;
import com.project.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.project.admin.catalogo.infrastructure.category.models.UpdateCategoryApiInput;
import com.project.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import com.project.admin.catalogo.infrastructure.category.retrieve.get.RetrieveCategoryByIdUseCase;
import com.project.admin.catalogo.infrastructure.category.retrieve.get.RetrieveCategoryOutput;
import com.project.admin.catalogo.infrastructure.category.retrieve.list.CategoryListOutput;
import com.project.admin.catalogo.infrastructure.category.retrieve.list.ListCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.update.UpdateCategoryOutput;
import com.project.admin.catalogo.infrastructure.category.update.UpdateCategoryUseCase;
import io.vavr.API;

import io.vavr.control.Either;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.List;
import java.util.Objects;

import static io.vavr.API.Left;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//faz o scan somente do que ta configurado como controller ou rest controller
//nao faz o scan dos components e services
@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;


    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private RetrieveCategoryByIdUseCase retrieveCategoryByIdUseCase;

    @MockBean
    UpdateCategoryUseCase updateCategoryUseCase;


    @MockBean
    DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    ListCategoryUseCase listCategoryUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws  Exception{
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aInput =
                new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);


        when(createCategoryUseCase.execute(any()))
                .thenReturn(API.Right(CreateCategoryOutput.from(CategoryID.from("123"))));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location", "/categories/123"),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE ),
                        jsonPath("$.id", equalTo("123"))
                );

        Mockito.verify(createCategoryUseCase, times(1)).execute(argThat(
                cmd->   Objects.equals(expectedName, cmd.name()) &&
                        Objects.equals(expectedDescription, cmd.description()) &&
                        Objects.equals(expectedIsActive, cmd.isActive())

        ));

    }


    @Test
    public void givenAInvalidName_whenCallCreateCategory_thenShouldReturnNotification() throws  Exception{
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var aInput =
                new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error("'name' should not be null"))));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("Location", Matchers.nullValue()),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE ),
                        jsonPath("$.errors", Matchers.hasSize(1)),
                        jsonPath("$.errors[0].message", equalTo(expectedMessage))
                );

        Mockito.verify(createCategoryUseCase, times(1)).execute(argThat(
                cmd->   Objects.equals(expectedName, cmd.name()) &&
                        Objects.equals(expectedDescription, cmd.description()) &&
                        Objects.equals(expectedIsActive, cmd.isActive())

        ));

    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateCategory_shouldReturnDomainException() throws  Exception{
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var aInput =
                new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error("'name' should not be null")));

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(aInput));

        this.mvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("Location", Matchers.nullValue()),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE ),
                        jsonPath("$.errors", Matchers.hasSize(1)),
                        jsonPath("$.errors[0].message", equalTo(expectedMessage))
                );

        Mockito.verify(createCategoryUseCase, times(1)).execute(argThat(
                cmd->   Objects.equals(expectedName, cmd.name()) &&
                        Objects.equals(expectedDescription, cmd.description()) &&
                        Objects.equals(expectedIsActive, cmd.isActive())

        ));

    }

    @Test
    public void givenACategoryId_whenCallFindById_thenReturnCategory() throws Exception {

        // given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId().getValue();

        when(retrieveCategoryByIdUseCase.execute(any()))
                .thenReturn(RetrieveCategoryOutput.from(aCategory));

        // when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

       // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id",equalTo(expectedId)))
                .andExpect(jsonPath("$.name",equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt())));



        verify(retrieveCategoryByIdUseCase, times(1)).execute(eq(expectedId));

    }


    @Test
    public void givenAInvalidId_whenCallFindById_thenReturnNotFound() throws Exception {

        // given
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");

        when(retrieveCategoryByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        // when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }


    // Update

    @Test
    public void givenAValidCommand_whenCallUpdateCategory_shouldReturnCategoryId() throws Exception {

        // given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        when(updateCategoryUseCase.execute(any()))
                .thenReturn(API.Right(UpdateCategoryOutput.from(expectedId)));


        final var aCommand = new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        // when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));


        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd->
                Objects.equals(expectedName, cmd.name()) &&
                Objects.equals(expectedDescription, cmd.description()) &&
                Objects.equals(expectedIsActive, cmd.isActive())));

    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_thenReturnNotFoundException() throws Exception {

        // given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with ID 123 was not found";


        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));


        final var aCommand = new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        // when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));


        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd->
                Objects.equals(expectedName, cmd.name()) &&
                        Objects.equals(expectedDescription, cmd.description()) &&
                        Objects.equals(expectedIsActive, cmd.isActive())));

    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {

        // given
        final var expectedId = "123";
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;


        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));


        final var aCommand = new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        // when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));


        verify(updateCategoryUseCase, times(expectedErrorCount)).execute(argThat(cmd->
                Objects.equals(expectedName, cmd.name()) &&
                        Objects.equals(expectedDescription, cmd.description()) &&
                        Objects.equals(expectedIsActive, cmd.isActive())));

    }

    //Delete
    @Test
    public void givenAValidId_whenCallsDeleteCategory_thenShouldReturnNoContent() throws Exception {

        // given
        final var expectedId ="123";

        doNothing().when(deleteCategoryUseCase).execute(any());

        // when
        final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNoContent());


        verify(deleteCategoryUseCase, times(1)).execute(eq(CategoryID.from(expectedId)));

    }


    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_thenShouldNoContent  () throws Exception {

        // given
        final var expectedId ="123";

        doNothing().when(deleteCategoryUseCase).execute(any());

        // when
        final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNoContent());


        verify(deleteCategoryUseCase, times(1)).execute(eq(CategoryID.from(expectedId)));

    }


    //List

    @Test
    public void givenAValidParams_whenCallsListCategories_shouldReturnCategories() throws Exception {

        final var aCategory = Category.newCategory("Movies", null, true);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ji1j3i 1j3i1oj";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CategoryListOutput.from(aCategory));

        when(listCategoryUseCase.execute(any())).thenReturn(new Pagination<>(expectedPage, expectedPerPage,expectedTotal,expectedItems));

        final var request = MockMvcRequestBuilders.get("/categories")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("direction", expectedDirection)
                .queryParam("search",expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", equalTo(expectedPage)))
                .andExpect(jsonPath("$.perPage", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aCategory.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aCategory.getName())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aCategory.getDescription())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aCategory.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aCategory.getDeletedAt())));


        verify(listCategoryUseCase, times(1)).execute(argThat(query->
                Objects.equals(expectedPage, query.page()) &&
                Objects.equals(expectedPerPage, query.perPage()) &&
                Objects.equals(expectedTerms, query.terms()) &&
                Objects.equals(expectedDirection, query.direction()) &&
                Objects.equals(expectedSort, query.sort())));
    }


}
