package io.oacy.education.springbootnoob.compnent;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

public class Cache {

    private Map<String,Object> map = new HashMap<String,Object>();

    public void set(String key,String value) {
        this.map.put(key,value);
    }

    public Object get(String key) {
        return this.map.get(key);
    }
}

@Configuration
class CacheConfiguration {

    @Bean
    public Cache createCacheObj() {
        return new Cache();
    }
}