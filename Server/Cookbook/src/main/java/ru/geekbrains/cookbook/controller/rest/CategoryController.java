package ru.geekbrains.cookbook.controller.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.geekbrains.cookbook.dto.CategoryDto;
import ru.geekbrains.cookbook.dto.CategoryDtoIn;
import ru.geekbrains.cookbook.service.CategoryService;
import java.net.URI;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/category")
@Tag(name = "Категории блюд", description = "API для категорий блюд")
public class CategoryController {
    private CategoryService categoryService;

    @Operation(summary = "Возвращает список категорий")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно",
                    content = { @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CategoryDto.class)))}
            )}
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<?> getAllCategories(@Parameter(description = "Идентификатор родительской категории", required = false) @RequestParam(value = "parentId", required = false) Long parentId,
                                              @Parameter(description = "Признак наличия дочерних категорий", required = false) @RequestParam(value = "childs", required = false) Boolean childs){
        return ResponseEntity.ok(categoryService.findAll(parentId, childs));
    }

    @Operation(summary = "Возвращает категорию по идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Указан некорректный идентификатор категории"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена")
    })
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{category_id}")
    public ResponseEntity<?> getCategoryByID(@Parameter(description = "Идентификатор категории", required = true) @PathVariable(value="category_id") Long categoryID){
        return ResponseEntity.ok(categoryService.getCategoryById(categoryID));
    }

    @Operation(summary = "Создает новую категорию", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Операция выполнена успешно", headers = @Header(name = "Location", description = "URI новой категории")),
            @ApiResponse(responseCode = "405", description = "Некорректные входные данные")
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<?> addCategory(@Parameter(description = "Новая категория", required = true) @RequestBody CategoryDtoIn categoryDtoIn) {
        categoryDtoIn.setId(null);
        CategoryDto category = categoryService.saveCategory(categoryDtoIn);
        URI newCategoryURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{category_id}")
                .buildAndExpand(category.getId())
                .toUri();
        return ResponseEntity.created(newCategoryURI).build();
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
                                            @Parameter(description = "Обновленные параметры категории", required = true) @RequestBody CategoryDtoIn categoryDtoIn) {
        categoryService.saveCategory(categoryDtoIn);
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

}
