package ru.geekbrains.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.cookbook.domain.RecipeRating;

@Repository
public interface RecipeRatingRepository extends JpaRepository<RecipeRating, Long> {
}
