package ru.geekbrains.cookbook.mapper;

import ru.geekbrains.cookbook.domain.RecipeRating;
import ru.geekbrains.cookbook.dto.RecipeRatingDto;

public class RecipeRatingMapper {
    public static RecipeRatingDto recipeRatingToDto (RecipeRating recipeRating){
        if(recipeRating == null)
            return null;
        return new RecipeRatingDto(recipeRating.getRatings(), recipeRating.getTotalRating());
    }
}
