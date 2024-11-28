package com.project.admin.catalogo.infrastructure.api;

import com.project.admin.catalogo.domain.pagination.Pagination;
import com.project.admin.catalogo.infrastructure.category.models.CategoryAPIOutput;
import com.project.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.project.admin.catalogo.infrastructure.category.models.UpdateCategoryApiInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "categories")
public interface CategoryAPI {
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> createCategory(@RequestBody  CreateCategoryApiInput input);

    @GetMapping
    Pagination<?> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    CategoryAPIOutput findById(@PathVariable(name = "id") String id);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateById(@PathVariable("id") String id , @RequestBody UpdateCategoryApiInput input);

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    void deletedById(@PathVariable("id") String id);
}
