package ru.geekbrains.cookbook.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.cookbook.controller.rest.response.ErrorResponse;
import ru.geekbrains.cookbook.controller.rest.response.OKResponse;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.RecipeIngredient;
import ru.geekbrains.cookbook.service.RecipeService;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/recipe")
public class RecipeController {
    private RecipeService recipeService;

    @GetMapping
    public List<Recipe> getAllRecipes(){
        return recipeService.findAll();
    }

    @GetMapping("/{recipe_id}")
    public Recipe getRecipeByID(@PathVariable(value="recipe_id") Long recipeID){
        return recipeService.getRecipeById(recipeID);
    }

    @PostMapping
    public ResponseEntity<OKResponse> addRecipe(@RequestBody Recipe recipe) {
        System.out.println("Rest Recipe POST: " + recipe);
        recipe.setId(null);
        recipe = recipeService.saveRecipe(recipe, null);
        return new ResponseEntity<>(new OKResponse(recipe.getId(), System.currentTimeMillis()), HttpStatus.CREATED);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<OKResponse> updateRecipe(@RequestBody Recipe recipe) {
        System.out.println("Rest Recipe PUT: " + recipe);
        recipe = recipeService.saveRecipe(recipe, null);
        return new ResponseEntity<>(new OKResponse(recipe.getId(), System.currentTimeMillis()), HttpStatus.OK);
    }

    @DeleteMapping("/{recipe_id}")
    public ResponseEntity<OKResponse> deleteRecipeByID(@PathVariable(value="recipe_id") Long recipeID){
        System.out.println("Rest Recipe DELETE: " + recipeID);
        recipeService.removeRecipe(recipeID);
        return new ResponseEntity<>(new OKResponse(recipeID, System.currentTimeMillis()), HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        System.out.println("Rest Recipe ERROR");
        ErrorResponse ErrorResponse = new ErrorResponse();
        ErrorResponse.setMessage(e.getMessage());
        ErrorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(ErrorResponse, HttpStatus.NOT_FOUND);
    }
}

