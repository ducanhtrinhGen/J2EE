package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve images from a workspace-level folder `static/images` so existing files
        // (outside classpath) are available at `/images/{filename}`.
        // Try common locations so images are found regardless of working directory:
        // 1) classpath:/static/images/ (when packaged)
        // 2) file:static/images/ (relative to working directory at runtime)
        // 3) file:../../static/images/ (workspace-level folder used during development)
        registry.addResourceHandler("/images/**")
            .addResourceLocations(
                "classpath:/static/images/",
                "file:static/images/",
                "file:../../static/images/"
            );
    }
}
