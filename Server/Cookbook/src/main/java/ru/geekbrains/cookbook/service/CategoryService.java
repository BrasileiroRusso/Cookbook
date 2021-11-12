package ru.geekbrains.cookbook.service;

import ru.geekbrains.cookbook.domain.Category;
import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    Category getCategoryById(Long id);
    Category saveCategory(Category category);
    boolean removeCategory(Long id);
}
