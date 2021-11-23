package ru.geekbrains.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.cookbook.domain.Ingredient;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
