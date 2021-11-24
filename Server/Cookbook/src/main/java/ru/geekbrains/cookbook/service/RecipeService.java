package ru.geekbrains.cookbook.service;

import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.cookbook.domain.Category;
import ru.geekbrains.cookbook.domain.Recipe;
import java.util.List;

public interface RecipeService {
    List<Recipe> findAll();
    List<Recipe> findAll(Long categoryId, String titleRegex);
    Recipe getRecipeById(Long id);
    Recipe saveRecipe(Recipe recipe, MultipartFile image);
    boolean removeRecipe(Long id);
}
