package com.project.admin.catalogo.infrastructure;


import com.project.admin.catalogo.infrastructure.configuration.WebServerConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;


@SpringBootApplication

public class Main {
    public static void main(String[] args) {
        System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "development");
        //System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
        SpringApplication.run(WebServerConfig.class, args);

        //Nenhum profile setado,então cai para development

    }

//    @Bean
//    public ApplicationRunner runner(CategoryRepository repository){
//        return args -> {
//            List<CategoryJpaEntity> all = repository.findAll();
//
//            Category filmes = Category.newCategory("filmes", null, true);
//
//            repository.saveAndFlush(CategoryJpaEntity.from(filmes));
//
//            repository.deleteAll();
//        };
//    }

//    @Bean
//    @DependsOnDatabaseInitialization
//    public ApplicationRunner runner(
//            @Autowired CreateCategoryUseCase createCategoryUseCase,
//            @Autowired UpdateCategoryUseCase updateCategoryUseCase,
//            @Autowired DeleteCategoryUseCase deleteCategoryUseCase,
//            @Autowired ListCategoryUseCase listCategoryUseCase,
//            @Autowired RetrieveCategoryByIdUseCase retrieveCategoryByIdUseCase
//            ){
//        return args -> {};
//    }
}