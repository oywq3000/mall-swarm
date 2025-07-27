package com.oyproj.search.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * SpringDoc相关配置
 * Created by macro on 2024/3/5.
 */
@Configuration
public class SpringDocConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI mallSearchOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("mall搜索系统")
                        .description("mall搜索相关接口文档")
                        .version("v1.0.0"));
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //配置访问`/swagger-ui/`路径时可以直接跳转到`/swagger-ui/index.html`
        registry.addViewController("/swagger-ui/").setViewName("redirect:/swagger-ui/index.html");
    }


}

