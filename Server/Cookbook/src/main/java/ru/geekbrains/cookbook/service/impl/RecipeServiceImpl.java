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
    public RecipeDto saveRecipe(RecipeDto recipeDto) {
        Recipe recipe = RecipeMapper.DtoToRecipe(recipeDto);
        Recipe newRecipe = recipe;
        Set<RecipeIngredient> ingredients = recipe.getIngredients();
        Long recipeId;

        if(recipe.getId() != null){
            recipeId = recipe.getId();
            newRecipe = recipeRepository.findById(recipe.getId()).orElseThrow(RecipeNotFoundException::new);
            newRecipe.setCategory(recipe.getCategory());
            newRecipe.setDescription(recipe.getDescription());
            newRecipe.setTitle(recipe.getTitle());
            newRecipe.setUser(recipe.getUser());
            newRecipe.setSteps(recipe.getSteps());
            Set<RecipeIngredient.Id> ids = new HashSet<>();
            if(ingredients != null){
                ingredients.forEach(i -> {
                    i.getId().setRecipeId(recipeId);
                    i.getId().setIngredientId(i.getIngredient().getId());
                    ids.add(i.getId());
                });
            }
            if(newRecipe.getIngredients() != null){
                for(RecipeIngredient recipeIngredient: newRecipe.getIngredients()){
                    if(!ids.contains(recipeIngredient.getId()))
                        recipeIngredientRepository.deleteById(recipeIngredient.getId());
                }
            }
            newRecipe.setIngredients(ingredients);
            newRecipe = recipeRepository.save(newRecipe);
        }
        else{
            newRecipe.setIngredients(null);
            newRecipe = recipeRepository.save(newRecipe);
            recipeId = newRecipe.getId();
            if(ingredients != null){
                ingredients.forEach(i -> {
                    i.getId().setRecipeId(recipeId);
                    i.getId().setIngredientId(i.getIngredient().getId());
                });
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
