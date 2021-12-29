package ru.geekbrains.cookbook.service;

import ru.geekbrains.cookbook.domain.Ingredient;
import java.util.List;

public interface IngredientService {
    List<Ingredient> findAll();
    Ingredient getIngredientById(Long id);
    Ingredient saveIngredient(Ingredient ingredient);
    boolean removeIngredient(Long id);
}

