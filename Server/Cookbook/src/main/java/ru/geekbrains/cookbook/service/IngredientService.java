package ru.geekbrains.cookbook.service;

import ru.geekbrains.cookbook.dto.IngredientDto;
import java.util.List;

public interface IngredientService {
    List<IngredientDto> findAll();
    IngredientDto getIngredientById(Long id);
    IngredientDto saveIngredient(IngredientDto ingredient);
    boolean removeIngredient(Long id);
}

