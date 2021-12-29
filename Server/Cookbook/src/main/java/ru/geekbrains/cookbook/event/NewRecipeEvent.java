package ru.geekbrains.cookbook.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.geekbrains.cookbook.domain.Recipe;

public class NewRecipeEvent extends ApplicationEvent {
    @Getter
    private final Recipe recipe;

    public NewRecipeEvent(Recipe recipe) {
        super(recipe);
        this.recipe = recipe;
    }

}

