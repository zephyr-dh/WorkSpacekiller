package io.oacy.education.newbie.springbootnewbie.configurations.multiprofilebean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 使用不同的环境时，可以按照环境来装配 @Profile
 */

@Configuration
@Slf4j
public class BeanConfiguration {

    @Bean
    @Profile("development")
    public Runnable development(){
        log.info("装载了development环境的Bean");
        return ()->{};
    }


    @Bean
    @Profile("production")
    public Runnable production(){
        log.info("装载了production环境的Bean");
        return ()->{};
    }
}
