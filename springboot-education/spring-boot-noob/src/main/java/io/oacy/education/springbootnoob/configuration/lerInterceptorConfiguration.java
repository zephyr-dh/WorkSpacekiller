package io.oacy.education.springbootnoob.configuration;

import io.oacy.education.springbootnoob.interceptor.CustomInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class lerInterceptorConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private CustomInterceptor customInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(customInterceptor).addPathPatterns("/**/*");
    }
}
