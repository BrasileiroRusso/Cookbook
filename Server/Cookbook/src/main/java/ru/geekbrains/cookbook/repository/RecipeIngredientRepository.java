package ru.geekbrains.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.cookbook.domain.RecipeIngredient;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, RecipeIngredient.Id> {
}
