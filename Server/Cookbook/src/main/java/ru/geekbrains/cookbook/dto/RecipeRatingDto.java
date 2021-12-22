package ru.geekbrains.cookbook.dto;

import lombok.Data;

@Data
public class RecipeRatingDto {
    private long ratings;
    private long totalRating;
    private float averageRating;

    public RecipeRatingDto(long ratings, long totalRating){
        this.ratings = ratings;
        this.totalRating = totalRating;
        if(ratings > 0)
            averageRating = ((float) totalRating)/ratings;
    }
}
