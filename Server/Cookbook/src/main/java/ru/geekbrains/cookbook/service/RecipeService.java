package ru.geekbrains.cookbook.service;

import ru.geekbrains.cookbook.domain.Recipe;
import java.util.List;

public interface RecipeService {
    List<Recipe> findAll();
    Recipe getRecipeById(Long id);
    Recipe saveRecipe(Recipe recipe);
    boolean removeRecipe(Long id);
}
