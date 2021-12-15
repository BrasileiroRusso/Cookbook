package ru.geekbrains.cookbook.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {
    /*
    1) Избранное
    2) Тэги
    3) Лайки и дизлайки
    4)--- Документация Swagger
    5) Загрузка файлов и привязка к объектам
    6) Безопасность
    7) Веб-интерфейс
    8)---Пагинация
    9) Комментарии
     */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dataResourceName = "data";
        String dataFolder = "C:/Data";
        String dataPath = Paths.get(dataFolder).toFile().getAbsolutePath();

        registry.addResourceHandler("/" + dataResourceName + "/**")
                .addResourceLocations("file:/"+  dataPath + "/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

}

