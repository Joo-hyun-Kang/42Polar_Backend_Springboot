package com._polar._polar_backend_spring.v1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class V1WebConfig {
    @Autowired
    Environment env;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String FRONT_URL = env.getProperty("FRONT_URL");
                String NS_FRONT_URL = env.getProperty("NS_FRONT_URL");
                String LOCALHOST = env.getProperty("LOCALHOST");

                registry.addMapping("/**")
                        .allowedOrigins(
                                FRONT_URL,
                                NS_FRONT_URL,
                                LOCALHOST
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
