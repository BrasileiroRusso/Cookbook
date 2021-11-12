package ru.geekbrains.cookbook.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.cookbook.controller.rest.response.ErrorResponse;
import ru.geekbrains.cookbook.controller.rest.response.OKResponse;
import ru.geekbrains.cookbook.domain.Category;
import ru.geekbrains.cookbook.service.CategoryService;
import ru.geekbrains.cookbook.service.exception.CategoryCannotDeleteException;
import ru.geekbrains.cookbook.service.exception.CategoryNotFoundException;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/rest/category")
public class CategoryController {
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.findAll();
    }

    @GetMapping("/{category_id}")
    public Category getCategoryByID(@PathVariable(value="category_id") Long categoryID){
        return categoryService.getCategoryById(categoryID);
    }

    @PostMapping
    public ResponseEntity<OKResponse> addCategory(@RequestBody Category category) {
        System.out.println("Rest Category POST: " + category);
        category.setId(null);
        category = categoryService.saveCategory(category);
        return new ResponseEntity<>(new OKResponse(category.getId(), System.currentTimeMillis()), HttpStatus.CREATED);
    }

    @PutMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<OKResponse> updateCategory(@RequestBody Category category) {
        System.out.println("Rest Category PUT: " + category);
        category = categoryService.saveCategory(category);
        return new ResponseEntity<>(new OKResponse(category.getId(), System.currentTimeMillis()), HttpStatus.OK);
    }

    @DeleteMapping("/{category_id}")
    public ResponseEntity<OKResponse> deleteCategoryByID(@PathVariable(value="category_id") Long categoryID){
        System.out.println("Rest Category DELETE: " + categoryID);
        categoryService.removeCategory(categoryID);
        return new ResponseEntity<>(new OKResponse(categoryID, System.currentTimeMillis()), HttpStatus.OK);
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
