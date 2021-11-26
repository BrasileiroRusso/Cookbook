package ru.geekbrains.cookbook.mapper;

import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.dto.RecipeDto;

public class RecipeMapper {
    public static RecipeDto recipeToDto(Recipe recipe){
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(recipe.getId());
        recipeDto.setCategory(recipe.getCategory());
        recipeDto.setTitle(recipe.getTitle());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setUser(recipe.getUser());
        recipeDto.setIngredients(recipe.getIngredients());
        recipeDto.setImagePath(recipe.getImagePath());
        recipeDto.setSteps(MapperUtil.mapToList(recipe.getSteps()));

        return recipeDto;
    }

    public static Recipe DtoToRecipe(RecipeDto recipeDto){
        Recipe recipe = new Recipe();
        recipe.setId(recipeDto.getId());
        recipe.setCategory(recipeDto.getCategory());
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setUser(recipeDto.getUser());
        recipe.setIngredients(recipeDto.getIngredients());
        recipe.setImagePath(recipeDto.getImagePath());
        recipe.setSteps(MapperUtil.listToMap(recipeDto.getSteps()));

        return recipe;
    }
}
