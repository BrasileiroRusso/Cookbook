package ru.geekbrains.cookbook.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.geekbrains.cookbook.domain.HashTag;
import ru.geekbrains.cookbook.dto.RecipeDto;
import java.util.*;

public interface RecipeService {
    List<RecipeDto> findAll();
    Page<RecipeDto> findAll(Pageable pageable, Long categoryId, String titleRegex);
    RecipeDto getRecipeById(Long id);
    RecipeDto saveRecipe(RecipeDto recipe);
    boolean removeRecipe(Long id);
    Set<HashTag> getTagsById(Long recipeId);
    boolean addTagToRecipe(Long recipeId, Long tagId);
    boolean removeTagFromRecipe(Long recipeId, Long tagId);
}
