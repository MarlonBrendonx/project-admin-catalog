package com.project.admin.catalogo.infrastructure.category.models;


import com.project.admin.catalogo.infrastructure.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JacksonTest
public class UpdateCategoryApiInputTest {

    @Autowired
    JacksonTester<UpdateCategoryApiInput> json;


    @Test
    public void testMarshall() throws IOException {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = false;


        final var response = new UpdateCategoryApiInput(
                expectedName,
                expectedDescription,
                expectedActive
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedActive);

    }

    @Test
    public void testUnmarshall() throws IOException {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedActive = false;

        final var json = """
                {
                    "name": "%s",
                    "description": "%s",
                    "is_active": "%s"
                }
                """.formatted(
                expectedName,
                expectedDescription,
                expectedActive);

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedActive);

    }
}
