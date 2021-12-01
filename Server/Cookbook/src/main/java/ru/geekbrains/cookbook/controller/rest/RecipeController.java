package ru.geekbrains.cookbook.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.cookbook.controller.rest.response.ErrorResponse;
import ru.geekbrains.cookbook.controller.rest.response.OKResponse;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.dto.RecipeDto;
import ru.geekbrains.cookbook.service.RecipeService;
import ru.geekbrains.cookbook.service.UploadFileService;
import java.util.List;
import java.util.function.Consumer;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/recipe")
public class RecipeController {
    private RecipeService recipeService;
    private UploadFileService uploadFileService;

    @GetMapping
    public List<RecipeDto> getAllRecipes(@RequestParam(value = "categoryId", required = false) Long categoryId,
                                         @RequestParam(value = "name", required = false) String name) {
        return recipeService.findAll(categoryId, name);
    }

    @GetMapping("/{recipe_id}")
    public RecipeDto getRecipeByID(@PathVariable(value="recipe_id") Long recipeID){
        return recipeService.getRecipeById(recipeID);
    }

    @PostMapping
    public ResponseEntity<OKResponse> addRecipe(@RequestBody RecipeDto recipeDto) {
        System.out.println("Rest Recipe POST: " + recipeDto);
        recipeDto.setId(null);
        recipeDto = recipeService.saveRecipe(recipeDto, null);
        return new ResponseEntity<>(new OKResponse(recipeDto.getId(), System.currentTimeMillis()), HttpStatus.CREATED);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<OKResponse> updateRecipe(@RequestBody RecipeDto recipeDto) {
        System.out.println("Rest Recipe PUT: " + recipeDto);
        recipeDto = recipeService.saveRecipe(recipeDto, null);
        return new ResponseEntity<>(new OKResponse(recipeDto.getId(), System.currentTimeMillis()), HttpStatus.OK);
    }

    @DeleteMapping("/{recipe_id}")
    public ResponseEntity<OKResponse> deleteRecipeByID(@PathVariable(value="recipe_id") Long recipeID){
        System.out.println("Rest Recipe DELETE: " + recipeID);
        recipeService.removeRecipe(recipeID);
        return new ResponseEntity<>(new OKResponse(recipeID, System.currentTimeMillis()), HttpStatus.OK);
    }

    @GetMapping("/{recipe_id}/image")
    public ResponseEntity<?> getAllImages(@PathVariable(value="recipe_id") Long recipeId) {
        LinkedFiles linkedFiles = uploadFileService.getUploadedFileListByResource(recipeId, Recipe.class);
        linkedFiles = FileController.transformUriInLinkedFiles(linkedFiles);
        return ResponseEntity.ok().body(linkedFiles);
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

