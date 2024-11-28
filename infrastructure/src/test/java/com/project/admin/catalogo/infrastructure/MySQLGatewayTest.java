package com.project.admin.catalogo.infrastructure;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.Tag;
import java.lang.annotation.*;


@Target(ElementType.TYPE) // Diz ao jvm quando e como usar as anotações
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@DataJpaTest // foi usado o datajpatest por o @SpringBootTest carrega todo contexto, vem vez disso o datajpatest nao,
//porem, ele nao reconheçe a anotação @componente ou @service
@ComponentScan(
        includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*MySqlGateway")
}) //dizendo ao scan do spring para carregar tudo que termina com MySqlGateway para corrigir o problema acima
@ExtendWith(CleanUpExtension.class)
@Tag("integrationTest")
public @interface MySQLGatewayTest {


}
