package ru.geekbrains.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.repository.RecipeRepository;
import ru.geekbrains.cookbook.service.RecipeService;
import ru.geekbrains.cookbook.service.exception.RecipeNotFoundException;
import java.util.List;

@Service("recipeService")
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    @Transactional
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);
    }

    @Override
    public Recipe saveRecipe(Recipe recipe) {
        recipe = recipeRepository.save(recipe);
        return recipe;
    }

    @Override
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
