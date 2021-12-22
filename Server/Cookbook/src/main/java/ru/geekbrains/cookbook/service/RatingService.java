package ru.geekbrains.cookbook.service;

import ru.geekbrains.cookbook.domain.UserRating;
import java.util.List;

public interface RatingService {
    List<UserRating> getAllRecipeRatings(Long recipeId);
    UserRating getRecipeRatingByUserId(Long recipeId, Long userId);
    UserRating saveRating(Long recipeId, Long userId, int rate);
    void removeRating(Long recipeId, Long userId);
}
