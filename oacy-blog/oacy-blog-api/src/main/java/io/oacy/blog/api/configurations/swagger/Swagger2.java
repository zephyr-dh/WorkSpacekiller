package io.oacy.blog.api.configurations.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <br>这两个注解，一个是swagger2的配置，一个是项目启动的时候启动swagger2.<br>具体什么意思看下代码就知道了。
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    /**
     *     swagger2的配置文件，这里可以配置swagger2的一些基本的内容，比如扫描的包等等
     */

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为当前包路径
                .apis(RequestHandlerSelectors.basePackage("io.oacy.blog.api.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * 构建 api文档的详细信息函数,注意这里的注解引用的是哪个
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title(" blog.oacy.io RESTful API")
                //创建人
                .contact(new Contact("Zephyr Dong", "http://zephyr.js.org", "zephyr@oacy.io"))
                //版本号
                .version("1.0")
                //描述
                .description("API 描述")
                .build();
    }
}
