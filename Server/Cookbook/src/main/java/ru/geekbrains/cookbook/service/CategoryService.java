package ru.geekbrains.cookbook.service;

import ru.geekbrains.cookbook.dto.CategoryDto;
import ru.geekbrains.cookbook.dto.CategoryDtoIn;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();
    List<CategoryDto> findAll(Long parentId, Boolean childs);
    CategoryDto getCategoryById(Long id);
    CategoryDto saveCategory(CategoryDtoIn category);
    boolean removeCategory(Long id);
}
