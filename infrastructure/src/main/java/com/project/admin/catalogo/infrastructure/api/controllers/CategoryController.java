package com.project.admin.catalogo.infrastructure.api.controllers;

import com.project.admin.catalogo.domain.category.CategoryID;
import com.project.admin.catalogo.domain.category.CategorySearchQuery;
import com.project.admin.catalogo.domain.pagination.Pagination;
import com.project.admin.catalogo.domain.validation.handler.Notification;
import com.project.admin.catalogo.infrastructure.api.CategoryAPI;
import com.project.admin.catalogo.infrastructure.category.create.CreateCategoryCommand;
import com.project.admin.catalogo.infrastructure.category.create.CreateCategoryOutput;
import com.project.admin.catalogo.infrastructure.category.create.CreateCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.delete.DeleteCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.models.CategoryAPIOutput;
import com.project.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.project.admin.catalogo.infrastructure.category.models.UpdateCategoryApiInput;
import com.project.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import com.project.admin.catalogo.infrastructure.category.retrieve.get.RetrieveCategoryByIdUseCase;
import com.project.admin.catalogo.infrastructure.category.retrieve.list.ListCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.update.UpdateCategoryCommand;
import com.project.admin.catalogo.infrastructure.category.update.UpdateCategoryOutput;
import com.project.admin.catalogo.infrastructure.category.update.UpdateCategoryUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;


@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final RetrieveCategoryByIdUseCase retrieveCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private  final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoryUseCase listCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase,
                              final RetrieveCategoryByIdUseCase retrieveCategoryByIdUseCase, UpdateCategoryUseCase updateCategoryUseCase, DeleteCategoryUseCase deleteCategoryUseCase, ListCategoryUseCase listCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.retrieveCategoryByIdUseCase = Objects.requireNonNull(retrieveCategoryByIdUseCase);
        this.updateCategoryUseCase =  Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.listCategoryUseCase = listCategoryUseCase;
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryApiInput input) {

        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true);

        final Function<Notification, ResponseEntity<?>> onError = notification ->
            ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/"+output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand).fold(onError,onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return  listCategoryUseCase.execute(new CategorySearchQuery(page, perPage, search, sort, direction))
                .map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryAPIOutput findById(final String id) {
        return CategoryApiPresenter.present(retrieveCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id,final UpdateCategoryApiInput input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true);

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand).fold(onError,onSuccess);
    }

    @Override
    public void deletedById(final  String id) {
        this.deleteCategoryUseCase.execute(CategoryID.from(id));
    }


}
