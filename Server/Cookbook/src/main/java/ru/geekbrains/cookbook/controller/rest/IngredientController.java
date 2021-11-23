package ru.geekbrains.cookbook.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.cookbook.controller.rest.response.ErrorResponse;
import ru.geekbrains.cookbook.controller.rest.response.OKResponse;
import ru.geekbrains.cookbook.domain.Ingredient;
import ru.geekbrains.cookbook.service.IngredientService;
import ru.geekbrains.cookbook.service.exception.IngredientCannotDeleteException;
import ru.geekbrains.cookbook.service.exception.IngredientNotFoundException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/ingredient")
public class IngredientController {
    private IngredientService ingredientService;

    @GetMapping
    public List<Ingredient> getAllIngredients(){
        return ingredientService.findAll();
    }

    @GetMapping("/{ingredient_id}")
    public Ingredient getIngredientByID(@PathVariable(value="ingredient_id") Long ingredientID){
        return ingredientService.getIngredientById(ingredientID);
    }

    @PostMapping
    public ResponseEntity<OKResponse> addIngredient(@RequestBody Ingredient ingredient) {
        System.out.println("Rest Ingredient POST: " + ingredient);
        ingredient.setId(null);
        ingredient = ingredientService.saveIngredient(ingredient);
        return new ResponseEntity<>(new OKResponse(ingredient.getId(), System.currentTimeMillis()), HttpStatus.CREATED);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<OKResponse> updateIngredient(@RequestBody Ingredient ingredient) {
        System.out.println("Rest Ingredient PUT: " + ingredient);
        ingredient = ingredientService.saveIngredient(ingredient);
        return new ResponseEntity<>(new OKResponse(ingredient.getId(), System.currentTimeMillis()), HttpStatus.OK);
    }

    @DeleteMapping("/{ingredient_id}")
    public ResponseEntity<OKResponse> deleteIngredientByID(@PathVariable(value="ingredient_id") Long ingredientID){
        System.out.println("Rest Ingredient DELETE: " + ingredientID);
        ingredientService.removeIngredient(ingredientID);
        return new ResponseEntity<>(new OKResponse(ingredientID, System.currentTimeMillis()), HttpStatus.OK);
    }

    @ExceptionHandler({IngredientNotFoundException.class, IngredientCannotDeleteException.class})
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        System.out.println("Rest Ingredient ERROR");
        ErrorResponse ErrorResponse = new ErrorResponse();
        ErrorResponse.setMessage(e.getMessage());
        ErrorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(ErrorResponse, HttpStatus.NOT_FOUND);
    }
}
