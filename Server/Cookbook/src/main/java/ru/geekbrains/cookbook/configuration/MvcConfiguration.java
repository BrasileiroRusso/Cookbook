package ru.geekbrains.cookbook.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dataResourceName = "data";
        String dataPath = Paths.get(System.getProperty("user.dir"), dataResourceName).toFile().getAbsolutePath();

        registry.addResourceHandler("/" + dataResourceName + "/**")
                .addResourceLocations("file:/"+  dataPath + "/");
    }

}

