package com.satheesh.employee.management.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebCofig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){

        registry.addMapping("/api/**")
                .allowedOrigins(
                        "http://localhost:4200",
                        "https://employee-management-ui-ohwz.onrender.com"
                )
                .allowedMethods(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
                .allowedHeaders("*");
    }
}
