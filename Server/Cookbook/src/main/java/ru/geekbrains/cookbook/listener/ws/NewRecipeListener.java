package ru.geekbrains.cookbook.listener.ws;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.geekbrains.cookbook.configuration.properties.WsProperties;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.event.NewRecipeEvent;

@Component("wsNewRecipeListener")
@RequiredArgsConstructor
public class NewRecipeListener implements ApplicationListener<NewRecipeEvent> {
    private final WsProperties props;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    public void onApplicationEvent(NewRecipeEvent event) {
        Recipe recipe = event.getRecipe();
        simpMessagingTemplate.convertAndSend(props.getBroker() + props.getNewRecipes(), recipe);
    }

}


