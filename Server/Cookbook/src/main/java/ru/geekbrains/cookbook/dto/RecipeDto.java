package ru.geekbrains.cookbook.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.cookbook.auth.User;
import ru.geekbrains.cookbook.domain.Category;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.RecipeIngredient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class RecipeDto {
    private Long id;
    private Category category;
    private String title;
    private String description;
    private User user;
    private Set<RecipeIngredient> ingredients = new HashSet<>();
    private List<Recipe.RecipeStep> steps = new ArrayList<>();
}
