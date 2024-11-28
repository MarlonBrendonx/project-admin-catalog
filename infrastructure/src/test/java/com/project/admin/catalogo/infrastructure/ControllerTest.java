package com.project.admin.catalogo.infrastructure;

import com.project.admin.catalogo.infrastructure.configuration.ObjectMapperConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.lang.annotation.*;

@Target(ElementType.TYPE) // Diz ao jvm quando e como usar as anotações
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@EnableWebMvc
@WebMvcTest //usado para teste em controller, serialização e desserialização, só injeta controller e restcontrollers
@Import(ObjectMapperConfig.class)
public @interface ControllerTest {

    @AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
    Class<?>[] controllers() default {};
}
