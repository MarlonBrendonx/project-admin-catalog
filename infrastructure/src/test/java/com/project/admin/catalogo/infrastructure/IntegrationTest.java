package com.project.admin.catalogo.infrastructure;


import com.project.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;


@Target(ElementType.TYPE) // Diz ao jvm quando e como usar as anotações
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@SpringBootTest(classes = WebServerConfig.class)
@ExtendWith(CleanUpExtension.class)
@Tag("integrationTest")
public @interface IntegrationTest {


}
