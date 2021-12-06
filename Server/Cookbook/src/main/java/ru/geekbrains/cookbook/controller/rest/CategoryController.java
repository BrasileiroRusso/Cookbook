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
import ru.geekbrains.cookbook.domain.Category;
import ru.geekbrains.cookbook.service.CategoryService;
import ru.geekbrains.cookbook.service.exception.CategoryCannotDeleteException;
import ru.geekbrains.cookbook.service.exception.CategoryNotFoundException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/category")
@Tag(name = "Категории блюд", description = "API для категорий блюд")
public class CategoryController {
    private CategoryService categoryService;

    @Operation(summary = "Возвращает список категорий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Category.class)))}
            )}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.findAll();
    }

    @Operation(summary = "Возвращает категорию по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор категории", content = @Content),
            @ApiResponse(responseCode = "404", description = "Категория не найдена", content = @Content)
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{category_id}")
    public Category getCategoryByID(@Parameter(description = "Идентификатор категории", required = true) @PathVariable(value="category_id") Long categoryID){
        return categoryService.getCategoryById(categoryID);
    }

    @Operation(summary = "Создает новую категорию", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Category.class))
                    }),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResponseEntity<?> addCategory(@Parameter(description = "Новая категория", required = true) @RequestBody Category category) {
        category.setId(null);
        category = categoryService.saveCategory(category);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Обновляет существующую категорию", description = "Обновляет существующую категорию с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена"),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{category_id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateCategory(@Parameter(description = "Идентификатор категории", required = true) @PathVariable(value="category_id") Long categoryId,
                                            @Parameter(description = "Обновленные параметры категории", required = true) @RequestBody Category category) {
        categoryService.saveCategory(category);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаляет категорию", description = "Удаляет категорию с заданным идентификатором")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно"),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{category_id}")
    public ResponseEntity<?> deleteCategoryByID(@Parameter(description = "Идентификатор удаляемой категории", required = true) @PathVariable(value="category_id") Long categoryID){
        categoryService.removeCategory(categoryID);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({CategoryNotFoundException.class, CategoryCannotDeleteException.class})
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        System.out.println("Rest Category ERROR");
        ErrorResponse ErrorResponse = new ErrorResponse();
        ErrorResponse.setMessage(e.getMessage());
        ErrorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(ErrorResponse, HttpStatus.NOT_FOUND);
    }
}
