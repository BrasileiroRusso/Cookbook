package ru.geekbrains.cookbook.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.RecipeIngredient;
import ru.geekbrains.cookbook.dto.RecipeDto;
import ru.geekbrains.cookbook.mapper.RecipeMapper;
import ru.geekbrains.cookbook.repository.RecipeIngredientRepository;
import ru.geekbrains.cookbook.repository.RecipeRepository;
import ru.geekbrains.cookbook.repository.specification.RecipeSpecification;
import ru.geekbrains.cookbook.service.RecipeService;
import ru.geekbrains.cookbook.service.exception.RecipeNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service("recipeService")
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {
    private final RecipeRepository recipeRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;

    @Override
    @Transactional
    public List<RecipeDto> findAll() {
        return recipeRepository.findAll().stream()
                .map(RecipeMapper::recipeToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<RecipeDto> findAll(Long categoryId, String titleRegex) {
        return recipeRepository.findAll(RecipeSpecification.recipeFilter(categoryId, titleRegex)).
                stream().
                map(RecipeMapper::recipeToDto).
                collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RecipeDto getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(RecipeNotFoundException::new);
        return RecipeMapper.recipeToDto(recipe);
    }

    @Override
    @Transactional
    public RecipeDto saveRecipe(RecipeDto recipeDto, MultipartFile image) {
        Recipe recipe = RecipeMapper.DtoToRecipe(recipeDto);
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

        if(ingredients != null){
            for(RecipeIngredient recipeIngredient: ingredients){
                recipeIngredient.getId().setRecipeId(newRecipe.getId());
                recipeIngredient.getId().setIngredientId(recipeIngredient.getIngredient().getId());
            }
            newRecipe.setIngredients(ingredients);
            recipeIngredientRepository.saveAll(newRecipe.getIngredients());
        }

        return RecipeMapper.recipeToDto(newRecipe);
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
