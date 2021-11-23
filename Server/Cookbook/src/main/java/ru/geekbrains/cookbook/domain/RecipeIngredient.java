package ru.geekbrains.cookbook.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "recipe_ingredient")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecipeIngredient {
    @EmbeddedId
    @JsonIgnore
    private Id id = new Id();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", insertable = false, updatable = false)
    @JsonBackReference
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", insertable = false, updatable = false)
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "unit_id", nullable = false)
    private Unit unit;

    @Column(name = "amount")
    private BigDecimal amount;

    public RecipeIngredient(Recipe recipe, Ingredient ingredient, Unit unit, BigDecimal amount){
        this.recipe = recipe;
        this.ingredient = ingredient;
        this.unit = unit;
        this.amount = amount;
        this.id.ingredientId = ingredient.getId();
        this.id.recipeId = recipe.getId();
    }

    @Embeddable
    @NoArgsConstructor
    public static class Id implements Serializable {
        @Column(name = "recipe_id")
        private Long recipeId;

        @Column(name = "ingredient_id")
        private Long ingredientId;

        public Id(Long ingredientId, Long recipeId){
            this.ingredientId = ingredientId;
            this.recipeId = recipeId;
        }

        public void setRecipeId(Long recipeId) {
            this.recipeId = recipeId;
        }

        public void setIngredientId(Long ingredientId) {
            this.ingredientId = ingredientId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Id id = (Id) o;
            return Objects.equals(ingredientId, id.ingredientId) && Objects.equals(recipeId, id.recipeId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(ingredientId, recipeId);
        }

        @Override
        public String toString() {
            return "Id{recipeId=" + recipeId + ", ingredientId=" + ingredientId + "}";
        }
    }

}
