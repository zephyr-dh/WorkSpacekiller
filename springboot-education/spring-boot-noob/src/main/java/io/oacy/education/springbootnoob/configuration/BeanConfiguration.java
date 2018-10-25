package io.oacy.education.springbootnoob.configuration;

import io.oacy.education.springbootnoob.filter.TimeFilter;
import io.oacy.education.springbootnoob.listener.CustomListener;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class BeanConfiguration {

    /**
     * 指定配置下才会装配这个Bean
     * @return
     */
    @Bean
    @Profile("development")
    public Runnable getRunnableDevelopment(){
        return ()->{};
    }

    @Bean
    @Profile("production")
    public Runnable getRunnableProduction(){
        return ()->{};
    }

    /**
     * 注册过滤器
     * 添加到过滤器链中，此方式适用于使用第三方的过滤器。
     * @return
     */
//    @Bean
//    public FilterRegistrationBean timeFilter() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//
//        TimeFilter timeFilter = new TimeFilter();
//        registrationBean.setFilter(timeFilter);
//
//        List<String> urls = new ArrayList<>();
//        urls.add("/*");
//        registrationBean.setUrlPatterns(urls);
//
//        return registrationBean;
//    }
//
//    @Bean
//    public ServletListenerRegistrationBean<CustomListener> servletListenerRegistrationBean(){
//        return new ServletListenerRegistrationBean<>(new CustomListener());
//    }
}
