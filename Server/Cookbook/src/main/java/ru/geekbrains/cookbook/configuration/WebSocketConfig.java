package ru.geekbrains.cookbook.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import ru.geekbrains.cookbook.configuration.properties.WsProperties;

@Configuration
@EnableWebSocketMessageBroker
@EnableConfigurationProperties(WsProperties.class)
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final WsProperties props;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(props.getBroker());
        config.setApplicationDestinationPrefixes(props.getAppPrefix());
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(props.getEndpoint())
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

}