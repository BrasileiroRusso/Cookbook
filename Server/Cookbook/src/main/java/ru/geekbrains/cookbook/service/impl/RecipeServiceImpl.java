package ru.geekbrains.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.cookbook.component.ImageService;
import ru.geekbrains.cookbook.domain.Category;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.RecipeIngredient;
import ru.geekbrains.cookbook.repository.RecipeIngredientRepository;
import ru.geekbrains.cookbook.repository.RecipeRepository;
import ru.geekbrains.cookbook.repository.specification.RecipeSpecification;
import ru.geekbrains.cookbook.service.CategoryService;
import ru.geekbrains.cookbook.service.RecipeService;
import ru.geekbrains.cookbook.service.exception.RecipeNotFoundException;
import java.util.*;

@Service("recipeService")
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final ImageService imageService;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    @Transactional
    public List<Recipe> findAll(Long categoryId, String titleRegex) {
        Category category = null;
        if(categoryId != null)
            category = categoryService.getCategoryById(categoryId);
        return recipeRepository.findAll(RecipeSpecification.recipeFilter(category, titleRegex));
    }

    @Override
    @Transactional
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);
    }

    @Override
    @Transactional
    public Recipe saveRecipe(Recipe recipe, MultipartFile image) {
        Recipe newRecipe = recipe;
        Set<RecipeIngredient> ingredients = recipe.getIngredients();
        if(recipe.getId() != null){
            newRecipe = recipeRepository.findById(recipe.getId()).orElseThrow(RecipeNotFoundException::new);
            newRecipe.setCategory(recipe.getCategory());
            newRecipe.setDescription(recipe.getDescription());
            newRecipe.setTitle(recipe.getTitle());
            newRecipe.setUser(recipe.getUser());
            newRecipe.setSteps(recipe.getSteps());
        }
        newRecipe = recipeRepository.save(newRecipe);

        if (image != null && !image.isEmpty()) {
            String pathImage = imageService.saveImage(image, "recipe");
            newRecipe.setImagePath(pathImage);
            newRecipe = recipeRepository.save(newRecipe);
            System.out.println("Recipe saved: " + newRecipe);
        }

        if(ingredients != null){
            for(RecipeIngredient recipeIngredient: ingredients){
                recipeIngredient.getId().setRecipeId(newRecipe.getId());
                recipeIngredient.getId().setIngredientId(recipeIngredient.getIngredient().getId());
            }
            newRecipe.setIngredients(ingredients);
            recipeIngredientRepository.saveAll(newRecipe.getIngredients());
        }

        return newRecipe;
    }

    @Override
    @Transactional
    public boolean removeRecipe(Long id) {
        try{
            recipeRepository.deleteById(id);
            return true;
        }
        catch(EmptyResultDataAccessException e){
            RecipeNotFoundException exc = new RecipeNotFoundException(String.format("Category with ID=%d doesn't exist", id));
            exc.initCause(e);
            throw exc;
        }
    }
}
