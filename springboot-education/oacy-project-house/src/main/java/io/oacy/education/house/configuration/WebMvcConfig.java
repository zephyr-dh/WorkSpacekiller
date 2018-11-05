package io.oacy.education.house.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

/**
 * Created by donghua on 2018/11/5
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        super.setApplicationContext(applicationContext);
        this.applicationContext = applicationContext;
    }

    /**
     * 模板资源解析器
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.thymeleaf")
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        return templateResolver;
    }

    /**
     * Thymeleaf标准方言解析器
     *
     * @return
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        //支持Spring EL表达式
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    /**
     * 视图解析器
     */
    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }
}
