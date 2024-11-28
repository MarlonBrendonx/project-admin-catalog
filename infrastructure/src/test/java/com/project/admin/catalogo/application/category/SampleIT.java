package com.project.admin.catalogo.application.category;

import com.project.admin.catalogo.infrastructure.IntegrationTest;
import com.project.admin.catalogo.infrastructure.category.create.CreateCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIT {

    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void  testInjects(){
        Assertions.assertNotNull(categoryRepository);
        Assertions.assertNotNull(categoryRepository);
    }

}
