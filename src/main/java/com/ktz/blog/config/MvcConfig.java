package com.ktz.blog.config;

import com.ktz.blog.web.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 曾泉明
 * @date 2020/2/24 13:33
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${file.savedPath}")
    private String savedPath;

    @Value("${file.urlPath}")
    private String urlPath;

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/admin/**")
                        .excludePathPatterns("/admin").excludePathPatterns("/admin/login");
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/static/**").addResourceLocations("classpath:/web/img/");
                registry.addResourceHandler(urlPath + "**").addResourceLocations("file:" + savedPath);
            }
        };
    }
}
