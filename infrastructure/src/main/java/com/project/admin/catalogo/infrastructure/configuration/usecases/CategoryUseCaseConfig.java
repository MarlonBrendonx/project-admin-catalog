package com.project.admin.catalogo.infrastructure.configuration.usecases;

import com.project.admin.catalogo.domain.category.CategoryGateway;
import com.project.admin.catalogo.infrastructure.category.create.CreateCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.create.DefaultCreateCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.delete.DefaultDeleteCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.delete.DeleteCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.retrieve.get.DefaultRetrieveCategoryByIdUseCase;
import com.project.admin.catalogo.infrastructure.category.retrieve.get.RetrieveCategoryByIdUseCase;
import com.project.admin.catalogo.infrastructure.category.retrieve.list.DefaultListCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.retrieve.list.ListCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.update.DefaultUpdateCategoryUseCase;
import com.project.admin.catalogo.infrastructure.category.update.UpdateCategoryUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(){
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase(){
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public RetrieveCategoryByIdUseCase retrieveCategoryByIdUseCase(){
        return new DefaultRetrieveCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoryUseCase listCategoryUseCase(){
        return new DefaultListCategoryUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(){
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }

}
