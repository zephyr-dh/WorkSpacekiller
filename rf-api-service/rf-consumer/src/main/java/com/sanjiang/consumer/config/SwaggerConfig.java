package com.sanjiang.consumer.config;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author kimiyu
 * @date 2018/4/24 18:17
 */
@Configuration
@EnableSwagger2
//@Profile(value = {"dev"})
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage("com.sanjiang.consumer.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RF枪接口API")
                .description("RF枪接口文档说明")
                .version("1.0")
                .build();
    }

    //    @Bean
    UiConfiguration uiConfiguration() {
        return UiConfigurationBuilder.builder()
                .validatorUrl(null)
                .tagsSorter(TagsSorter.ALPHA)
                .defaultModelRendering(ModelRendering.of("schema"))
                .docExpansion(DocExpansion.LIST)
                .displayRequestDuration(true)
                .build();
    }

}
