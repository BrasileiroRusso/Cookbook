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
import ru.geekbrains.cookbook.domain.Ingredient;
import ru.geekbrains.cookbook.service.IngredientService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/ingredient")
@Tag(name = "Ингредиенты", description = "API для ингредиентов")
public class IngredientController {
    private IngredientService ingredientService;

    @Operation(summary = "Возвращает список ингредиентов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Ingredient.class)))}
            )}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Ingredient> getAllIngredients(){
        return ingredientService.findAll();
    }

    @Operation(summary = "Возвращает категорию по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Ingredient.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Ингредиент не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{ingredient_id}")
    public Ingredient getIngredientByID(@Parameter(description = "Идентификатор ингредиента", required = true) @PathVariable(value="ingredient_id") Long ingredientID){
        return ingredientService.getIngredientById(ingredientID);
    }

    @Operation(summary = "Создает новый ингредиент", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Ingredient.class))
                    }),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResponseEntity<?> addIngredient(@Parameter(description = "Новый ингредиент", required = true) @RequestBody Ingredient ingredient) {
        ingredient.setId(null);
        ingredient = ingredientService.saveIngredient(ingredient);
        return ResponseEntity.ok(ingredient);
    }

    @Operation(summary = "Обновляет существующий ингредиент", description = "Обновляет существующий ингредиент с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Ингредиент не найден"),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{ingredient_id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateIngredient(@Parameter(description = "Идентификатор ингредиента", required = true) @PathVariable(value="ingredient_id") Long ingredientId,
                                              @Parameter(description = "Обновленные параметры ингредиента", required = true) @RequestBody Ingredient ingredient) {
        ingredient = ingredientService.saveIngredient(ingredient);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаляет ингредиент", description = "Удаляет ингредиент с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Ингредиент не найден")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{ingredient_id}")
    public ResponseEntity<?> deleteIngredientByID(@Parameter(description = "Идентификатор удаляемого ингредиента", required = true) @PathVariable(value="ingredient_id") Long ingredientID){
        ingredientService.removeIngredient(ingredientID);
        return ResponseEntity.ok().build();
    }

}
