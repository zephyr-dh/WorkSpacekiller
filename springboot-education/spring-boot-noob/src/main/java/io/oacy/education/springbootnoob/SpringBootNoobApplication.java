package io.oacy.education.springbootnoob;

import io.oacy.education.springbootnoob.compnent.DataSourceProperties;
import io.oacy.education.springbootnoob.conditionConfiguration.EncodingConverter;
import io.oacy.education.springbootnoob.configuration.DataSourceConfiguration;
import io.oacy.education.springbootnoob.filter.TimeFilter;
import io.oacy.education.springbootnoob.listener.CustomListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;
import java.util.Map;

@SpringBootApplication
@EnableScheduling
public class SpringBootNoobApplication implements ServletContextInitializer {

    public static void main(String[] args) {
//        SpringApplication.run(SpringBootNoobApplication.class, args);
        //获取上下文
        ConfigurableApplicationContext applicationContext=SpringApplication.run(SpringBootNoobApplication.class);
        //获得制定的Bean
        DataSourceConfiguration dataSourceConfiguration=applicationContext.getBean(DataSourceConfiguration.class);

        dataSourceConfiguration.show();
        /**
         * 测试装载自定义配置
         */
        applicationContext.getBean(DataSourceProperties.class).show();

        /**
         * 条件配置
         */
        Map<String, EncodingConverter> map = applicationContext.getBeansOfType(EncodingConverter.class);
        System.out.println(map);

    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // 配置 Servlet
//        servletContext.addServlet("servletTest",new ServletTest())
//                .addMapping("/servletTest");
        // 配置过滤器
        servletContext.addFilter("timeFilter",new TimeFilter())
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST),true,"/*");
        // 配置监听器
        servletContext.addListener(new CustomListener());
    }
}
