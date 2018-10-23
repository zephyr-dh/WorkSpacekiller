package io.oacy.education.springbootnoob.configuration;

import io.oacy.education.springbootnoob.conditionConfiguration.EncodingConverter;
import io.oacy.education.springbootnoob.conditionConfiguration.implement.GBKEncodingConverter;
import io.oacy.education.springbootnoob.conditionConfiguration.implement.UTF8EncodingConverter;
import io.oacy.education.springbootnoob.configuration.conditions.GBKCondition;
import io.oacy.education.springbootnoob.configuration.conditions.UTF8Condition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncodingConverterConfiguration {

    @Bean
    @Conditional(UTF8Condition.class)
    public EncodingConverter createUTF8EncodingConverter() {
        return new UTF8EncodingConverter();
    }

    @Bean
    @Conditional(GBKCondition.class)
    public EncodingConverter createGBKEncodingConverter() {
        return new GBKEncodingConverter();
    }
}
