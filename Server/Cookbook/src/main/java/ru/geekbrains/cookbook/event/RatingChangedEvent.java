package ru.geekbrains.cookbook.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.geekbrains.cookbook.domain.UserRating;

public class RatingChangedEvent extends ApplicationEvent {
    @Getter
    private final String recipeTitle;
    @Getter
    private final String username;
    @Getter
    private final String email;
    @Getter
    private final UserRating rating;

    public RatingChangedEvent(UserRating rating, String recipeTitle, String username, String email) {
        super(rating);
        this.recipeTitle = recipeTitle;
        this.username = username;
        this.email = email;
        this.rating = rating;
    }

}

