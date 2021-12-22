package ru.geekbrains.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import ru.geekbrains.cookbook.auth.User;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_rating")
@NoArgsConstructor
public class UserRating {
    @EmbeddedId
    @JsonIgnore
    private Id id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", insertable = false, updatable = false)
    @JsonIgnore
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "rate")
    private int rate;

    public UserRating(Long recipeId, Long userId){
        id = new Id(recipeId, userId);
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Id implements Serializable {
        @Column(name = "recipe_id")
        private Long recipeId;

        @Column(name = "user_id")
        private Long userId;

        public Long getRecipeId() {
            return recipeId;
        }

        public void setRecipeId(Long recipeId) {
            this.recipeId = recipeId;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }
    }

}
