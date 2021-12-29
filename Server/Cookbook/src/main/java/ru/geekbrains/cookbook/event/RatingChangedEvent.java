package ru.geekbrains.cookbook.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.UserRating;

public class RatingChangedEvent extends ApplicationEvent {
    @Getter
    private final Recipe recipe;
    @Getter
    private final User user;
    @Getter
    private final UserRating rating;

    public RatingChangedEvent(Recipe recipe, User user, UserRating rating) {
        super(user);
        this.recipe = recipe;
        this.user = user;
        this.rating = rating;
    }

}

