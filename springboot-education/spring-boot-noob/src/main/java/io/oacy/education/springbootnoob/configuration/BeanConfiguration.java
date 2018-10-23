package io.oacy.education.springbootnoob.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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


}
