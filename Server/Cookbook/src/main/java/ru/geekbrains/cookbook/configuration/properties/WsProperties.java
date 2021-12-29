package ru.geekbrains.cookbook.configuration.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ws")
@NoArgsConstructor
@Data
public class WsProperties {
    private String appPrefix;
    private String userPrefix;
    private String broker;
    private String endpoint;
    private String newRecipes;
    private String ratings;
}
