package com.hedgerock.app_server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    //TODO REPLACE FOR CONSTANT VALUE (NOT SAFETY)
    @Value("${client.port.value}")
    private String PORT;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedMethods(HttpMethod.PUT.name(), HttpMethod.GET.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name())
                .allowedOrigins(
                        String.format("http://localhost:%s", PORT),
                        "http://cloud_client:4200",
                        "http://localhost:4200"
                )
                .allowCredentials(true)
                .maxAge(3600);
    }

}
