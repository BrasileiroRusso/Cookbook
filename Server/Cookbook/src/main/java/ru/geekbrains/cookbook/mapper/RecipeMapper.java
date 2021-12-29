package ru.geekbrains.cookbook.mapper;

import ru.geekbrains.cookbook.controller.rest.RecipeController;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.dto.RecipeDto;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class RecipeMapper {
    public static RecipeDto recipeToDto(Recipe recipe){
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setId(recipe.getId());
        recipeDto.setCategory(recipe.getCategory());
        recipeDto.setTitle(recipe.getTitle());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setUser(recipe.getUser());
        recipeDto.setIngredients(recipe.getIngredients());
        recipeDto.setSteps(MapperUtil.mapToList(recipe.getSteps()));
        recipeDto.setRating(RecipeRatingMapper.recipeRatingToDto(recipe.getRating()));
        recipeDto.setTags(recipe.getTags());
        recipeDto.setPrepareTime(recipe.getPrepareTime());
        recipeDto.setComment(recipe.getComment());

        recipeDto.add(linkTo(methodOn(RecipeController.class).getRecipeByID(recipe.getId()))
                .withSelfRel());
        recipeDto.add(linkTo(methodOn(RecipeController.class).getAllImages(recipe.getId()))
                .withRel("images"));
        recipeDto.add(linkTo(methodOn(RecipeController.class).getAllRatings(recipe.getId()))
                .withRel("ratings"));
        recipeDto.add(linkTo(methodOn(RecipeController.class).getAllTags(recipe.getId()))
                .withRel("tags"));

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
        recipe.setSteps(MapperUtil.listToMap(recipeDto.getSteps()));
        recipe.setPrepareTime(recipeDto.getPrepareTime());
        recipe.setComment(recipeDto.getComment());

        return recipe;
    }
}
