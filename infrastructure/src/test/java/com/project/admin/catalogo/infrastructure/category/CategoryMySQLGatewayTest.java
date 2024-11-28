package com.project.admin.catalogo.infrastructure.category;

import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryID;
import com.project.admin.catalogo.domain.category.CategorySearchQuery;
import com.project.admin.catalogo.infrastructure.MySQLGatewayTest;
import com.project.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.project.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    CategoryMySqlGateway categoryMySqlGateway;

    @Autowired
    CategoryRepository categoryRepository;

//    @BeforeEach
//    public void cleanUp(){
//        this.categoryRepository.deleteAll();
//    }

    @Test
    public void givenAValidCategory_whenCallsCreate_ShouldReturnANewCategory() {
        final var expectedName = "filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedActive);
        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryMySqlGateway.create(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var anEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), anEntity.getId());
        Assertions.assertEquals(expectedName, anEntity.getName());
        Assertions.assertEquals(expectedDescription, anEntity.getDescription());
        Assertions.assertEquals(expectedActive, anEntity.getActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), anEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), anEntity.getUpdatedAt());
        Assertions.assertNull(anEntity.getDeletedAt());


    }

    @Test
    public void givenAValidCategory_whenCallsUpdated_ShouldReturnACategoryUpdated() {

        final var expectedName = "filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        Category aCategory = Category.newCategory("Film", null, expectedActive);
        Assertions.assertEquals(0, categoryRepository.count());

        //save and assert
        final var actualCategory = categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(aCategory.getId().getValue(), actualCategory.getId());
        Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive(), actualCategory.getActive());

        //update
        final var aUpdatedCategory = aCategory.clone().update(expectedName, expectedDescription, expectedActive);

        final var categoryUpdated = categoryMySqlGateway.update(aUpdatedCategory);

        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(aCategory.getId().getValue(), categoryUpdated.getId().getValue());
        Assertions.assertEquals(expectedName, categoryUpdated.getName());
        Assertions.assertEquals(expectedDescription, categoryUpdated.getDescription());
        Assertions.assertEquals(expectedActive, categoryUpdated.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), categoryUpdated.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(categoryUpdated.getUpdatedAt()));
        Assertions.assertNull(categoryUpdated.getDeletedAt());


        final var actualEntityAfterUpdated = categoryRepository.findById(categoryUpdated.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntityAfterUpdated.getId());
        Assertions.assertEquals(expectedName, actualEntityAfterUpdated.getName());
        Assertions.assertEquals(expectedDescription, actualEntityAfterUpdated.getDescription());
        Assertions.assertEquals(expectedActive, actualEntityAfterUpdated.getActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntityAfterUpdated.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualEntityAfterUpdated.getUpdatedAt()));
        Assertions.assertNull(actualEntityAfterUpdated.getDeletedAt());


    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryDeletedIt_shouldDeleteCategory() {
        final var aCategory = Category.newCategory("filmes", "descricão", true);
        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());

        categoryMySqlGateway.deletedById(aCategory.getId());
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAInvalidCategoryId_whenTryDeletedIt_shouldDeleteCategory() {
        Assertions.assertEquals(0, categoryRepository.count());

        categoryMySqlGateway.deletedById(CategoryID.from("invalid"));

        Assertions.assertEquals(0, categoryRepository.count());
    }


    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_ShouldReturnACategory() {

        final var expectedName = "filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = true;

        Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedActive);
        Assertions.assertEquals(0, categoryRepository.count());

        //save and assert
        final var actualCategory = categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(aCategory.getId().getValue(), actualCategory.getId());
        Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive(), actualCategory.getActive());


        final var actualEntity = categoryMySqlGateway.findById(aCategory.getId()).get();

        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId().getValue());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }


    @Test
    public void givenAValidCategoryIdNotStored_whenCallsFindById_ShouldReturnEmpty() {
        Assertions.assertEquals(0, categoryRepository.count());

        final var actualEntity = categoryMySqlGateway.findById(CategoryID.from("invalid"));

        Assertions.assertEquals(0, categoryRepository.count());
        Assertions.assertTrue(actualEntity.isEmpty());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_ShouldReturnPaginated() {

        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;


        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentarios", null, true);


        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(categoryRepository.saveAll(
                List.of(
                        CategoryJpaEntity.from(filmes),
                        CategoryJpaEntity.from(series),
                        CategoryJpaEntity.from(documentarios)
                )
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name","asc");
        final var actualCategories = categoryMySqlGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, actualCategories.perPage());
        Assertions.assertEquals(expectedTotal, actualCategories.total());
        Assertions.assertEquals(expectedPerPage, actualCategories.items().size());
        Assertions.assertEquals(documentarios.getId(), actualCategories.items().get(0).getId());
    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_ShouldReturnEmpty() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;



        final var query = new CategorySearchQuery(0, 1, "", "name","asc");
        final var actualCategories = categoryMySqlGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, actualCategories.perPage());
        Assertions.assertEquals(expectedTotal, actualCategories.total());
        Assertions.assertEquals(expectedPage, actualCategories.items().size());
    }
    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage_ShouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;


        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentarios", null, true);


        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(categoryRepository.saveAll(
                List.of(
                        CategoryJpaEntity.from(filmes),
                        CategoryJpaEntity.from(series),
                        CategoryJpaEntity.from(documentarios)
                )
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        var query = new CategorySearchQuery(0, 1, "", "name","asc");
        var actualCategories = categoryMySqlGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, actualCategories.perPage());
        Assertions.assertEquals(expectedTotal, actualCategories.total());
        Assertions.assertEquals(expectedPerPage, actualCategories.items().size());
        Assertions.assertEquals(documentarios.getId(), actualCategories.items().get(0).getId());

         //Page 1
         expectedPage = 1;

         query = new CategorySearchQuery(1, 1, "", "name","asc");
         actualCategories = categoryMySqlGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, actualCategories.perPage());
        Assertions.assertEquals(expectedTotal, actualCategories.total());
        Assertions.assertEquals(expectedPerPage, actualCategories.items().size());
        Assertions.assertEquals(filmes.getId(), actualCategories.items().get(0).getId());

        //Page 2
        expectedPage = 2;

        query = new CategorySearchQuery(2, 1, "", "name","asc");
        actualCategories = categoryMySqlGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, actualCategories.perPage());
        Assertions.assertEquals(expectedTotal, actualCategories.total());
        Assertions.assertEquals(expectedPerPage, actualCategories.items().size());
        Assertions.assertEquals(series.getId(), actualCategories.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndDocsAsTerms_whenCallsFindAllAndTermsMatchsCategoryName_ShouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;


        final var filmes = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Series", null, true);
        final var documentarios = Category.newCategory("Documentarios", null, true);


        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(categoryRepository.saveAll(
                List.of(
                        CategoryJpaEntity.from(filmes),
                        CategoryJpaEntity.from(series),
                        CategoryJpaEntity.from(documentarios)
                )
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        var query = new CategorySearchQuery(0, 1, "doc", "name","asc");
        var actualCategories = categoryMySqlGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, actualCategories.perPage());
        Assertions.assertEquals(expectedTotal, actualCategories.total());
        Assertions.assertEquals(expectedPerPage, actualCategories.items().size());
        Assertions.assertEquals(documentarios.getId(), actualCategories.items().get(0).getId());

    }

    @Test
    public void givenPrePersistedCategoriesAndMaisAssistidasAsTerm_whenCallsFindAllAndTermsMatchsCategoryDescription_ShouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;


        final var filmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var series = Category.newCategory("Series", "A categoria mais ou menos assistida", true);
        final var documentarios = Category.newCategory("Documentarios", "A categoria mais ou menos assistida", true);


        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(categoryRepository.saveAll(
                List.of(
                        CategoryJpaEntity.from(filmes),
                        CategoryJpaEntity.from(series),
                        CategoryJpaEntity.from(documentarios)
                )
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        var query = new CategorySearchQuery(0, 1, "mais assistida", "name","asc");
        var actualCategories = categoryMySqlGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualCategories.currentPage());
        Assertions.assertEquals(expectedPerPage, actualCategories.perPage());
        Assertions.assertEquals(expectedTotal, actualCategories.total());
        Assertions.assertEquals(expectedPerPage, actualCategories.items().size());
        Assertions.assertEquals(filmes.getId(), actualCategories.items().get(0).getId());

    }

//    @Test
//    public void givenPrePersistedCategories_whenCallsExistsByIds_shouldReturnIds() {
//        // given
//        final var filmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
//        final var series = Category.newCategory("Séries", "Uma categoria assistida", true);
//        final var documentarios = Category.newCategory("Documentários", "A categoria menos assistida", true);
//
//        Assertions.assertEquals(0, categoryRepository.count());
//
//        categoryRepository.saveAll(List.of(
//                CategoryJpaEntity.from(filmes),
//                CategoryJpaEntity.from(series),
//                CategoryJpaEntity.from(documentarios)
//        ));
//
//        Assertions.assertEquals(3, categoryRepository.count());
//
//        final var expectedIds = List.of(filmes.getId(), series.getId());
//
//        final var ids = List.of(filmes.getId(), series.getId(), CategoryID.from("123"));
//
//        // when
//        final var actualResult = categoryMySqlGateway.existsByIds(ids);
//
//        Assertions.assertTrue(
//                expectedIds.size() == actualResult.size() &&
//                        expectedIds.containsAll(actualResult)
//        );
//    }
}