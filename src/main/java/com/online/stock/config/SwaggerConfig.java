package com.online.stock.config;

import com.google.common.base.Predicate;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.containsPattern;
import static com.google.common.base.Predicates.or;

@PropertySource("classpath:swagger.properties")
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket webApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.online.stock.controller"))
                .paths(paths())
                .build()
                .apiInfo(metaData())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo metaData() {
        return new ApiInfo(
                "Dr.Joy Web API",
                "Dr.Joy Web API Description",
                "1.0",
                "",
                null,
                "",
                "",
                Collections.emptyList());
    }

    private Predicate<String> paths() {
        return or(
                containsPattern("/*")
        );
    }
}