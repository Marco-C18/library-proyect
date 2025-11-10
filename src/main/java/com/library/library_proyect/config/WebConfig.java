package com.library.library_proyect.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login",
                        "/registro",
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/static/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapea las imágenes subidas en tiempo de ejecución
        registry.addResourceHandler("/img/libros/**")
                .addResourceLocations("file:src/main/resources/static/img/libros/");

        // Si quieres asegurar que todo el contenido estático se sirva correctamente
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}