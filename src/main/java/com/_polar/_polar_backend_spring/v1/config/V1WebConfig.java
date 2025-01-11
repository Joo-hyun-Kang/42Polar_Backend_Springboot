package com._polar._polar_backend_spring.v1.config;

import com._polar._polar_backend_spring.v1.auth.interceptors.AuthInterceptor;
import com._polar._polar_backend_spring.v1.auth.interceptors.RoleInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@AllArgsConstructor
public class V1WebConfig implements WebMvcConfigurer {
    private final Environment env;
    private final AuthInterceptor authInterceptor;

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

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .order(1)
                .addPathPatterns("/api/v1/**");

//        registry.addInterceptor(new RoleInterceptor())
//                .order(2)
//                .addPathPatterns("/api/v1/**");

    }
}
