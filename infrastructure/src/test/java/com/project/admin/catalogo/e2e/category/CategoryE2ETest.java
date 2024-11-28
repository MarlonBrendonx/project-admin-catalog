package com.project.admin.catalogo.e2e.category;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.project.admin.catalogo.domain.category.Category;
import com.project.admin.catalogo.domain.category.CategoryID;
import com.project.admin.catalogo.infrastructure.E2ETest;
import com.project.admin.catalogo.infrastructure.category.models.CategoryAPIOutput;
import com.project.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.project.admin.catalogo.infrastructure.category.models.UpdateCategoryApiInput;
import com.project.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.hamcrest.Matchers;
import springfox.documentation.spring.web.json.Json;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = new MySQLContainer("mysql:8.0")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);

        System.out.printf("Container is running on port %s\n", mappedPort );
        registry.add("mysql.port", () -> mappedPort);
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = retrieveACategory(actualId.getValue());


        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertNotNull(actualCategory.createdAt());
        Assertions.assertNotNull( actualCategory.updatedAt());
        Assertions.assertNull(actualCategory.deletedAt());


    }

    @Test
    public void asCatalogAdminIShouldBeAbleToNavigateToAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());


        givenACategory("Filmes", "filme descrição",true);
        givenACategory("Series", "serie descrição", true);
        givenACategory("Documentários", "documentrio descrição", true);

        listCategories(0,1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.currentPage",equalTo(0)))
                .andExpect(jsonPath("$.perPage",equalTo(1)))
                .andExpect(jsonPath("$.total",equalTo(3)))
                .andExpect(jsonPath("$.items",hasSize(1)))
                .andExpect(jsonPath("$.items[0].name",equalTo("Documentários")));

        listCategories(1,1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.currentPage",equalTo(1)))
                .andExpect(jsonPath("$.perPage",equalTo(1)))
                .andExpect(jsonPath("$.total",equalTo(3)))
                .andExpect(jsonPath("$.items",hasSize(1)))
                .andExpect(jsonPath("$.items[0].name",equalTo("Filmes")));

        listCategories(2,1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.currentPage",equalTo(2)))
                .andExpect(jsonPath("$.perPage",equalTo(1)))
                .andExpect(jsonPath("$.total",equalTo(3)))
                .andExpect(jsonPath("$.items",hasSize(1)))
                .andExpect(jsonPath("$.items[0].name",equalTo("Series")));

        listCategories(3,1)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.currentPage",equalTo(3)))
                .andExpect(jsonPath("$.perPage",equalTo(1)))
                .andExpect(jsonPath("$.total",equalTo(3)))
                .andExpect(jsonPath("$.items",hasSize(0)));

    }


    @Test
    public void asCatalogAdminIShouldBeAbleToSearchBetweenAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        givenACategory("Documentários", "documentrio descrição", true);
        givenACategory("Filmes", "filme descrição",true);
        givenACategory("Series", "serie descrição", true);


        listCategories(0,1, "fil")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.currentPage",equalTo(0)))
                .andExpect(jsonPath("$.perPage",equalTo(1)))
                .andExpect(jsonPath("$.total",equalTo(1)))
                .andExpect(jsonPath("$.items",hasSize(1)))
                .andExpect(jsonPath("$.items[0].name",equalTo("Filmes")));

    }


    @Test
    public void asCatalogAdminIShouldBeAbleToSortAllCategoriesByNameDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());


        givenACategory("Filmes", "filmes descrição",true);
        givenACategory("Series", "serie descrição", true);
        givenACategory("Documentários", "documentrio descrição", true);

        listCategories(0,3, "name","desc", "")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.currentPage",equalTo(0)))
                .andExpect(jsonPath("$.perPage",equalTo(3)))
                .andExpect(jsonPath("$.total",equalTo(3)))
                .andExpect(jsonPath("$.items",hasSize(3)))
                .andExpect(jsonPath("$.items[0].name",equalTo("Series")))
                .andExpect(jsonPath("$.items[1].name",equalTo("Filmes")))
                .andExpect(jsonPath("$.items[2].name",equalTo("Documentários")));

    }


    @Test
    public void asCatalogAdminIShouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());


        givenACategory("Filmes", "filmes descrição",true);
        givenACategory("Series", "serie descrição", true);
        givenACategory("Documentários", "documentrio descrição", true);

        listCategories(0,3, "description","desc", "")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.currentPage",equalTo(0)))
                .andExpect(jsonPath("$.perPage",equalTo(3)))
                .andExpect(jsonPath("$.total",equalTo(3)))
                .andExpect(jsonPath("$.items",hasSize(3)))
                .andExpect(jsonPath("$.items[0].name",equalTo("Series")))
                .andExpect(jsonPath("$.items[1].name",equalTo("Filmes")))
                .andExpect(jsonPath("$.items[2].name",equalTo("Documentários")));

    }

    @Test
    public void asCatalogAdminIShouldBeAbleToSortAllCategoriesByDescriptionAsc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());


        givenACategory("Filmes", "filmes descrição",true);
        givenACategory("Series", "serie descrição", true);
        givenACategory("Documentários", "documentrio descrição", true);

        listCategories(0,3, "description","asc", "")
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.currentPage",equalTo(0)))
                .andExpect(jsonPath("$.perPage",equalTo(3)))
                .andExpect(jsonPath("$.total",equalTo(3)))
                .andExpect(jsonPath("$.items",hasSize(3)))
                .andExpect(jsonPath("$.items[0].name",equalTo("Documentários")))
                .andExpect(jsonPath("$.items[1].name",equalTo("Filmes")))
                .andExpect(jsonPath("$.items[2].name",equalTo("Series")));

    }


    @Test
    public void asCatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, categoryRepository.count());

        final var aRequest = MockMvcRequestBuilders.get("/categories/1234")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("%s with ID %s was not found".formatted("Category", "1234"))));

    }

    @Test
    public void asCatalogAdminIShouldBeAbleToUpdateACategory() throws Exception {
        final var expectedName = "Filmes Top";
        final var expectedDescription = "A categoria mais assistida nesse mes";
        final var expectedIsActive = true;

        final var anId = givenACategory("Filmes", "A categoria mais assistida", true);


        Assertions.assertEquals(1, categoryRepository.count());

        final var aRequest = MockMvcRequestBuilders.put("/categories/"+anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive)
                ));

        this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk());


        final var actualCategory = categoryRepository.findById(anId.getValue());

        Assertions.assertEquals(expectedName, actualCategory.get().getName());
        Assertions.assertEquals(expectedDescription, actualCategory.get().getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.get().getActive());
        Assertions.assertNotNull(actualCategory.get().getUpdatedAt());
        Assertions.assertNotNull( actualCategory.get().getCreatedAt());
        Assertions.assertNull(actualCategory.get().getDeletedAt());

    }


    @Test
    public void asCatalogAdminIShouldBeAbleToUpdateACategoryActiveToInactive() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var anId = givenACategory(expectedName, expectedDescription, true);

        Assertions.assertEquals(1, categoryRepository.count());

        final var aRequest = MockMvcRequestBuilders.put("/categories/"+anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive)
                ));

        this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk());


        final var actualCategory = categoryRepository.findById(anId.getValue());

        Assertions.assertEquals(expectedName, actualCategory.get().getName());
        Assertions.assertEquals(expectedDescription, actualCategory.get().getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.get().getActive());
        Assertions.assertNotNull(actualCategory.get().getUpdatedAt());
        Assertions.assertNotNull( actualCategory.get().getCreatedAt());
        Assertions.assertNotNull(actualCategory.get().getDeletedAt());

    }

    @Test
    public void asCatalogAdminIShouldBeAbleToUpdateACategoryInactiveToActive() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var anId = givenACategory(expectedName, expectedDescription, false);

        Assertions.assertEquals(1, categoryRepository.count());

        final var aRequest = MockMvcRequestBuilders.put("/categories/"+anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive)
                ));

        this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk());


        final var actualCategory = categoryRepository.findById(anId.getValue());

        Assertions.assertEquals(expectedName, actualCategory.get().getName());
        Assertions.assertEquals(expectedDescription, actualCategory.get().getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.get().getActive());
        Assertions.assertNotNull(actualCategory.get().getUpdatedAt());
        Assertions.assertNotNull( actualCategory.get().getCreatedAt());
        Assertions.assertNull(actualCategory.get().getDeletedAt());

    }

    @Test
    public void asCatalogAdminIShouldBeAbleToDeletedCategoryById() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var anId = givenACategory(expectedName, expectedDescription, false);

        Assertions.assertTrue(categoryRepository.existsById(anId.getValue()));

        final var aRequest = MockMvcRequestBuilders.delete("/categories/"+anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Assertions.assertFalse(categoryRepository.existsById(anId.getValue()));
    }

    private ResultActions listCategories(final int page, final int perPage, final String sort, final String direction, final String search ) throws Exception {

        final var aRequest = MockMvcRequestBuilders.get("/categories")
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("sort", sort)
                .queryParam("direction", direction)
                .queryParam("search",search)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        return this.mvc.perform(aRequest);

    }


    private ResultActions listCategories(final int page, final int perPage, String sort, String direction ) throws Exception {
        return listCategories(page, perPage,  sort, direction, "");
    }

    private ResultActions listCategories(final int page, final int perPage, String search ) throws Exception {
        return listCategories(page, perPage,  "", "", search);
    }

    private ResultActions listCategories(final int page, final int perPage ) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    private CategoryAPIOutput retrieveACategory(final String anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.get("/categories/"+anId)
                .contentType(MediaType.APPLICATION_JSON);

        final var json = this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return objectMapper.readValue(json, CategoryAPIOutput.class);
    }

    private CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {

        final var requestBody = new CreateCategoryApiInput(aName, aDescription, isActive);

        final var aRequest = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));

        final var actualId = this.mvc.perform(aRequest)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("/categories/", "");

        return CategoryID.from(actualId);

    }


}
