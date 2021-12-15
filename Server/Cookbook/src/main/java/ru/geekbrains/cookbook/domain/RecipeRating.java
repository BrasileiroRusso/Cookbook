package ru.geekbrains.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "recipe_rating")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecipeRating {
    @Id
    @Column(name = "recipe_id")
    @JsonIgnore
    private Long recipeId;

    @Column(name = "ratings")
    private long ratings;

    @Column(name = "total_rating")
    private long totalRating;

    public RecipeRating(Long recipeId){
        this.recipeId = recipeId;
        ratings = 0;
        totalRating = 0;
    }

    public void addToTotalRating(int ratingDiff){
        totalRating += ratingDiff;
    }

    public void incRatings(){
        ratings++;
    }

    public void decRatings(){
        ratings--;
    }

}
