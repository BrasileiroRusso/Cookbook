package ru.geekbrains.cookbook.service;

import ru.geekbrains.cookbook.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();
    List<CategoryDto> findAll(Long parentId, Boolean childs);
    CategoryDto getCategoryById(Long id);
    CategoryDto saveCategory(CategoryDto categoryDto);
    boolean removeCategory(Long id);
}
