package ru.geekbrains.cookbook.service;

import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.cookbook.dto.RecipeDto;

import java.util.List;

public interface RecipeService {
    List<RecipeDto> findAll();
    List<RecipeDto> findAll(Long categoryId, String titleRegex);
    RecipeDto getRecipeById(Long id);
    RecipeDto saveRecipe(RecipeDto recipe);
    boolean removeRecipe(Long id);
}
