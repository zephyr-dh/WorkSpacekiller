package io.oacy.education.xunwu.configuration;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UtilsConfiguration {
    /**
     * 属性复制工具
     * @return
     */
    @Bean(name = "modelMapper")
    public ModelMapper getModelMapper(){
        return new ModelMapper();
    }

    /**
     * Json解析工具
     * @return
     */
    @Bean(name = "gson")
    public Gson getGson() {
        return new Gson();
    }
}
