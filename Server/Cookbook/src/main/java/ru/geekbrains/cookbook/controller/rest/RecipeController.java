package ru.geekbrains.cookbook.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.cookbook.controller.rest.response.ErrorResponse;
import ru.geekbrains.cookbook.domain.Recipe;
import ru.geekbrains.cookbook.domain.file.LinkedFiles;
import ru.geekbrains.cookbook.dto.RecipeDto;
import ru.geekbrains.cookbook.service.RecipeService;
import ru.geekbrains.cookbook.service.UploadFileService;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/recipe")
@Tag(name = "Рецепты", description = "API для рецептов")
public class RecipeController {
    private RecipeService recipeService;
    private UploadFileService uploadFileService;

    @Operation(summary = "Возвращает список рецептов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         content = { @Content(mediaType = "application/json",
                                              array = @ArraySchema(schema = @Schema(implementation = RecipeDto.class)))}
                        )}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<RecipeDto> getAllRecipes(@Parameter(description = "Идентификатор категории", required = true) @RequestParam(value = "categoryId", required = false) Long categoryId,
                                         @Parameter(description = "Фрагмент наимеонвания блюда", required = true) @RequestParam(value = "name", required = false) String name) {
        return recipeService.findAll(categoryId, name);
    }

    @Operation(summary = "Возвращает рецепт по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                         content = {
                                    @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class))
                         }),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор", content = @Content),
            @ApiResponse(responseCode = "404", description = "Рецепт не найден", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{recipe_id}")
    public RecipeDto getRecipeByID(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value="recipe_id") Long recipeID){
        return recipeService.getRecipeById(recipeID);
    }

    @Operation(summary = "Создает новый рецепт", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = RecipeDto.class))
                    }),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResponseEntity<?> addRecipe(@Parameter(description = "Новый рецепт", required = true) @RequestBody RecipeDto recipeDto) {
        recipeDto.setId(null);
        recipeDto = recipeService.saveRecipe(recipeDto);
        return ResponseEntity.ok(recipeDto);
    }

    @Operation(summary = "Обновляет существующий рецепт", description = "Обновляет существующий рецепт с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Рецепт не найден"),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{recipe_id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateRecipe(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value="recipe_id") Long recipeId,
                                          @Parameter(description = "Обновленные параметры рецепта", required = true) @RequestBody RecipeDto recipeDto) {
        recipeDto = recipeService.saveRecipe(recipeDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаляет рецепт", description = "Удаляет рецепт с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Рецепт не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{recipe_id}")
    public ResponseEntity<?> deleteRecipeByID(@Parameter(description = "Идентификатор удаляемого рецепта", required = true) @PathVariable(value="recipe_id") Long recipeID){
        recipeService.removeRecipe(recipeID);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Возвращает список изображений для рецепта", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LinkedFiles.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Некорректный идентификатор рецепта")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{recipe_id}/image")
    public ResponseEntity<?> getAllImages(@Parameter(description = "Идентификатор рецепта", required = true) @PathVariable(value="recipe_id") Long recipeId) {
        LinkedFiles linkedFiles = uploadFileService.getUploadedFileListByResource(recipeId, Recipe.class);
        linkedFiles = FileController.transformUriInLinkedFiles(linkedFiles);
        return ResponseEntity.ok().body(linkedFiles);
    }

    @PostMapping("/{recipe_id}/image")
    public ResponseEntity<?> addImage(){
        return null;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse ErrorResponse = new ErrorResponse();
        ErrorResponse.setMessage(e.getMessage());
        ErrorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(ErrorResponse, HttpStatus.NOT_FOUND);
    }
}

