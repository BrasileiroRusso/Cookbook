package ru.geekbrains.cookbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.cookbook.domain.UserRating;
import java.util.List;

@Repository
public interface UserRatingRepository extends JpaRepository<UserRating, UserRating.Id> {
    List<UserRating> findAllByRecipe_Id(Long recipeId);
    List<UserRating> findAllByUser_Id(Long userId);
}
